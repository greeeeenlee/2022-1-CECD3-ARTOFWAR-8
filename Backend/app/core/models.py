from django.db import models

# Create your models here.

class Videoinfo(models.Model):
    name = models.CharField(max_length=30)
    mjclass = models.CharField(db_column='mjClass', max_length=45, blank=True, null=True, default="0")  # Field name made lowercase.
    subclass = models.CharField(db_column='SubClass', max_length=45, blank=True, null=True, default="0")  # Field name made lowercase.
    status = models.CharField(max_length=45, blank=True, null=True, default="0")
    vdaddress = models.CharField(db_column='vdAddress', max_length=45, blank=True, null=True)  # Field name made lowercase.
    introduction = models.CharField(max_length=45, blank=True, null=True, default="0")

    class Meta:
        managed = False
        db_table = 'videoInfo'
    



