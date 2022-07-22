from django.db import models

class Inquire(models.Model):
    qid = models.AutoField(primary_key=True)
    name = models.CharField(max_length=100, blank=True, null=True)
    content = models.CharField(max_length=300, blank=True, null=True)
    vid = models.ForeignKey('Videoinfo', models.DO_NOTHING, db_column='vid')
    uid = models.ForeignKey('Userinfo', models.DO_NOTHING, db_column='uid')
    question = models.CharField(max_length=300, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'inquire'


class Userinfo(models.Model):
    ID = models.CharField(max_length=45)
    name = models.CharField(max_length=45)
    password = models.CharField(max_length=100)
    uid = models.AutoField(primary_key=True)

    class Meta:
        managed = False
        db_table = 'userInfo'


class Videoinfo(models.Model):
    vid = models.AutoField(primary_key=True)
    name = models.CharField(max_length=45, blank=True, null=True)
    mjclass = models.CharField(max_length=45, blank=True, null=True)
    subclass = models.CharField(max_length=45, blank=True, null=True)
    status = models.IntegerField(blank=True, null=True)
    introduction = models.CharField(max_length=45, blank=True, null=True)
    storage_key = models.CharField(max_length=45, blank=True, null=True)
    storage_url = models.CharField(max_length=45, blank=True, null=True)
    uid = models.ForeignKey(Userinfo, models.DO_NOTHING, db_column='uid')

    class Meta:
        managed = False
        db_table = 'videoInfo'