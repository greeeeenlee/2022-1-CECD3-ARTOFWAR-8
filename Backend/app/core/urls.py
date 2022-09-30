from django.urls import path ,include
from django.contrib import admin
from core import views

app_name = 'api'

urlpatterns = [
    path('publish/', views.my_pub_view.as_view(), name='publish'),
    path('sign', views.signUp.as_view(), name='signUp'),
    path('sign/<uid>', views.checkID.as_view(), name='checkID'),
    path('upload', views.uploadVideo.as_view(), name='uploadVideo'),
    path('user', views.user.as_view(), name='userFunction'),
    path('userInfo/<uid>', views.userInfo.as_view(), name='userInfo'),
    path('videoInfo/<vid>', views.videoInfo.as_view(), name='getVideoInfo'),
    path('deleteVideo/<vid>', views.deleteVideo.as_view(), name='deleteVideo'),
    path('main/user', views.userMain.as_view(), name='userMain'),
    path('main/admin', views.adminMain.as_view(), name='adminMain'),
    path('createInquire', views.createInquire.as_view(), name='createInquire'),
    path('inquireUser/<qid>', views.inquireUser.as_view(), name='inquireUser'),
    path('inquireAdmin/<qid>', views.inquireAdmin.as_view(), name='inquireAdmin'),
]