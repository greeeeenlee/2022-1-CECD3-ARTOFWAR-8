from  __future__  import  absolute_import 
import os
from celery import Celery
from django.conf import settings

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'app.settings')

app = Celery('app', broker="redis://redis_for_broker:6379", backend="redis://redis_for_broker:6379")
app.config_from_object('django.conf:settings', namespace='CELERY')
app.autodiscover_tasks(lambda: settings.INSTALLED_APPS)