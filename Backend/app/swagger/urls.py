from django.urls import path ,include
from django.contrib import admin
from swagger import views

app_name = 'swagger'

urlpatterns = [
    path('publish/', views.my_pub_view.as_view(), name='publish'),
    path('user/sign', views.signUp.as_view(), name='signUp'),
    path('user/checkID/<uid>', views.checkID.as_view(), name='checkID'),
    path('video/upload', views.uploadVideo.as_view(), name='uploadVideo'),
    path('user/log', views.user.as_view(), name='userFunction'),
    path('user/getInfo', views.userInfo.as_view(), name='userInfo'),
    path('user/changepwd', views.changepwd.as_view(), name='changepwd'),
    path('video/getList', views.videoInfo.as_view(), name='getVideoInfo'),
    path('video/changeInfo/<storage_key>', views.changeVideoInfo.as_view(), name='changeVideoInfo'),
    path('video/delete/<storage_key>', views.deleteVideo.as_view(), name='deleteVideo'),
    path('video/getNum', views.userMain.as_view(), name='userMain'),
    path('aInquire/nullQ', views.adminMain.as_view(), name='adminMain'),   
    path('uInquire/getList', views.getUserInquireInfo.as_view(), name='getUserInquireInfo'),
    path('aInquire/getList', views.getAdminInquireInfo.as_view(), name='getAdminInquireInfo'),
    path('uInquire/create/<storage_key>', views.createInquire.as_view(), name='createInquire'),
    path('uInquire/manage/<qid>', views.inquireUser.as_view(), name='inquireUser'),
    path('aInquire/manage/<qid>', views.inquireAdmin.as_view(), name='inquireAdmin'),
    path('uInquire/delete/<qid>', views.deleteInquire.as_view(), name='deleteInquire'),
]