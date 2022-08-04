from celery import shared_task
from django.template import base
from .models import Videoinfo
from django.core.files.storage import default_storage
import time
from django.conf import settings
import boto3
from botocore.exceptions import NoCredentialsError
import datetime
import requests

@shared_task
def add(x, y):
    time.sleep(10)
    return x + y

@shared_task
def upload_video(file):
    basename = 'video'
    suffix = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
    address = "_".join([basename, suffix])
    s3 = boto3.client('s3', aws_access_key_id=settings.ACCESS_KEY,
                      aws_secret_access_key=settings.SECRET_KEY)
    try:
        s3.upload_file(file, settings.BUCKET_NAME, address)        
    except FileNotFoundError:
        return False
    except NoCredentialsError:
        return False

    return address

@shared_task
def store_video(address,name,main_category,sub_category):
    #record = Videoinfo(name=name, vdaddress=url, storage_key=address)
    url = "/".join([settings.BUCKET_URL,address])
    record = Videoinfo(name=name, storage_url=url, storage_key=address,mjclass=main_category,subclass=sub_category)
    record.save()
    return record.get_vid()


@shared_task
def delete_video(address):
    s3 = boto3.client('s3', aws_access_key_id=settings.ACCESS_KEY,
                      aws_secret_access_key=settings.SECRET_KEY)
    try:
        s3.delete_object(settings.BUCKET_NAME, address)
        return True
    except FileNotFoundError:
        return False
    except NoCredentialsError:
        return False


@shared_task
def classify_video(storage_key,vid):
    try:
        response = requests.post('http://172.31.11.209:5000/classify', data={'title':storage_key})
        record = Videoinfo.objects.get(pk=vid)
        record.status=response
        record.save()
        return True
    except FileNotFoundError:
        return False
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

