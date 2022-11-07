from django.db import models
from django.contrib.auth.models import User
from django.utils import timezone
class Inquire(models.Model):
    qid = models.AutoField(primary_key=True)
    name = models.CharField(max_length=100, blank=True, null=True)
    content = models.CharField(max_length=300, blank=True, null=True)
    vid = models.ForeignKey('videoInfo', models.DO_NOTHING, db_column='vid')
    uid = models.ForeignKey('userInfo', models.DO_NOTHING, db_column='uid')
    question = models.CharField(max_length=300, blank=True, null=True)

    class Meta:
        managed = True
        db_table = 'inquire'


class Userinfo(models.Model):
    ID = models.CharField(max_length=45)
    name = models.CharField(max_length=45)
    password = models.CharField(max_length=100)
    uid = models.AutoField(primary_key=True)
    is_authenticated = models.BooleanField(default=True) 
    is_staff = models.BooleanField(default=False)

    class Meta:
        managed = True
        db_table = 'userInfo'

class Videoinfo(models.Model):
    vid = models.AutoField(primary_key=True)
    name = models.CharField(max_length=45, blank=True, null=True)
    mjclass = models.CharField(max_length=45, blank=True, null=True)
    subclass = models.CharField(max_length=45, blank=True, null=True)
    status = models.IntegerField(blank=True, null=True)
    status_detail = models.CharField(max_length=45, blank=True, null=True)
    image = models.IntegerField(blank=True, null=True)
    image_ext = models.CharField(max_length=45, blank=True, null=True)
    upload_time=models.DateTimeField(auto_now=True)
    introduction = models.CharField(max_length=45, blank=True, null=True)
    storage_key = models.CharField(max_length=45, blank=True, null=True)
    storage_url = models.CharField(max_length=45, blank=True, null=True)
    uid = models.ForeignKey('userInfo', models.DO_NOTHING, db_column='uid')

    class Meta:
        managed = True
        db_table = 'videoInfo'