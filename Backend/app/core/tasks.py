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
#import cv2

@shared_task
def upload_video(image, file):
    basename = 'video'
    suffix = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
    address = "_".join([basename, suffix])
    s3 = boto3.client('s3', aws_access_key_id=settings.ACCESS_KEY,
                      aws_secret_access_key=settings.SECRET_KEY)
    v_ext = os.path.splitext(file)[1]
    try:
        #if image==None:
            #vidcap = cv2.VideoCapture(file)
            #count=0
            #while(vidcap.isOpened()&count<1):
                #ret, image = vidcap.read()
                # 이미지 사이즈 960x540으로 변경
                #image = cv2.resize(image, (960, 540))
                
                # 30프레임 이미지 추출
                #if(int(vidcap.get(1)) % 30 == 0):
                    # 추출된 이미지가 저장
                    #cv2.imwrite(address+".png", image)
                    #count+=1
            #vidcap.release()

        s3.upload_file(file, settings.BUCKET_NAME, address+v_ext)
        s3.upload_file(image, settings.BUCKET_NAME, 'image_'+address+'.jpeg',ExtraArgs={'ContentType': "image/jpeg"})
        return address
    except FileNotFoundError:
        return False

@shared_task
def store_video(address,name,main_category,sub_category,uid):
    user=Userinfo.objects.get(ID=uid) 
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
def classify_video(storage_key,vid):
    try:
        data = requests.post('http://172.31.11.209:5000/classify', data={'title':storage_key}).json()
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

