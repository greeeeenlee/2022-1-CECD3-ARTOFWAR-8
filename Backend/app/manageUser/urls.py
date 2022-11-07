from django.urls import path ,include
from django.contrib import admin
from manageUser import views

app_name = 'manageUser'

urlpatterns = [
    path('sign', views.signUp.as_view(), name='signUp'),
    path('checkID/<uid>', views.checkID.as_view(), name='checkID'),
    path('log', views.log.as_view(), name='log'),
    path('getInfo', views.getInfo.as_view(), name='getInfo'),
    path('changepwd', views.changepwd.as_view(), name='changepwd'),
]