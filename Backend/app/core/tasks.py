from celery import shared_task
from django.template import base
from .models import Videoinfo,Userinfo
from django.core.files.storage import default_storage
import time,json,os.path
from django.conf import settings
import boto3
from botocore.exceptions import NoCredentialsError
import datetime
import requests

@shared_task
def upload_video(image, file):
    basename = 'video'
    suffix = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
    address = "_".join([basename, suffix])
    s3 = boto3.client('s3', aws_access_key_id=settings.ACCESS_KEY,
                      aws_secret_access_key=settings.SECRET_KEY)
    v_ext = os.path.splitext(file)[1]
    try:
        s3.upload_file(file, settings.BUCKET_NAME, address+v_ext)
        if image!='None':
            s3.upload_file(image, settings.BUCKET_NAME, 'image_'+address+'.jpeg',ExtraArgs={'ContentType': "image/jpeg"})

        return address+v_ext
    except FileNotFoundError:
        return False

@shared_task
def store_video(address,name,main_category,sub_category,uid):
    user=Userinfo.objects.get(uid=uid) 
    url = "/".join([settings.BUCKET_URL,address])
    Videoinfo.objects.create(name=name, storage_url=url, storage_key=address,
            mjclass=main_category,subclass=sub_category,uid=user)
    video=Videoinfo.objects.get(storage_key=address)
    return video.get_vid()

@shared_task
def delete_s3_video(address):
    s3 = boto3.client('s3', aws_access_key_id=settings.ACCESS_KEY,
                      aws_secret_access_key=settings.SECRET_KEY)
    try:
        s3.delete_object(Bucket=settings.BUCKET_NAME, Key=address)
        return True
    except FileNotFoundError:
        return False
    except NoCredentialsError:
        return False


@shared_task
def classify_video(storage_key,vid,image_file_name):
    try:
        if image_file_name=='None':
            data = requests.post('http://172.31.11.209:5000/classify', data={'title':storage_key,'image':'None'}).json()
        else:
            data = requests.post('http://172.31.11.209:5000/classify', data={'title':storage_key,'image':''}).json()
        video = Videoinfo.objects.filter(vid=vid)
        video.update(
            status=int(data['status'])
        )
        return True
    except response.raise_for_status():# 200 OK 코드가 아닌 경우 에러 발동
        return False

@shared_task
def analysis_video(storage_key,vid):
    try:
        response = requests.post('http://44.196.228.195:5000/analysis', data={'title':storage_key})
        response.json() # json response일 경우 딕셔너리 타입으로 바로 변환
        status=response['harmful']
        record = Videoinfo.objects.get(pk=vid)
        record.status=status
        record.save()
        
        keywords=response['keywords']
        dynamodb=boto3.resource('dynamodb')
        table=dynamodb.Table('vid_keyword')
        table.put_item(
            Item={
                'vid_title':storage_key,
                'keyword':keywords
                })
        return True
    except FileNotFoundError:
        return False
    except response.raise_for_status():# 200 OK 코드가 아닌 경우 에러 발동
        return False

