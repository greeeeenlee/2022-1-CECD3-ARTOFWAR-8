from django.urls import path ,include
from django.contrib import admin
from video import views

app_name = 'video'

urlpatterns = [
    path('upload', views.uploadVideo.as_view(), name='uploadVideo'),
    path('delete/<storage_key>', views.deleteVideo.as_view(), name='deleteVideo'),
    path('getList', views.getInfoList.as_view(), name='getInfoList'),
    path('changeInfo/<storage_key>', views.changeInfo.as_view(), name='changeInfo'),
    path('getNum', views.getNum.as_view(), name='getNum'),
]