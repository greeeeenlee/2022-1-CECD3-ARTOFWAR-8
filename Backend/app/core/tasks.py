from celery import shared_task
from django.template import base
from .models import Videoinfo
from django.core.files.storage import default_storage
import time
from django.conf import settings
import boto3
from botocore.exceptions import NoCredentialsError
import datetime

@shared_task
def add(x, y):
    time.sleep(10)
    return x + y

@shared_task
def upload_video(file, name):
    basename = 'video'
    suffix = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
    address = "_".join([basename, suffix])
    s3 = boto3.client('s3', aws_access_key_id=settings.ACCESS_KEY,
                      aws_secret_access_key=settings.SECRET_KEY)
    try:
        s3.upload_file(file, settings.BUCKET_NAME, address)
        url = "/".join([settings.BUCKET_URL,address])
        
    except FileNotFoundError:
        return False
    except NoCredentialsError:
        return False
    
    record = Videoinfo(name=name, vdaddress=url, storage_key=address)
    record.save()
    default_storage.delete(file)
    return address

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

