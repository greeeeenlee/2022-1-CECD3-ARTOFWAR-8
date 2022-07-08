from celery import shared_task
from .models import Videoinfo
from django.core.files.storage import default_storage
import time

@shared_task
def add(x, y):
    time.sleep(10)
    return x + y

@shared_task
def upload_video(file, name):
    address = 'abcd'
    record = Videoinfo(name=name, vdaddress=address)
    record.save()
    default_storage.delete(file)
    return address



