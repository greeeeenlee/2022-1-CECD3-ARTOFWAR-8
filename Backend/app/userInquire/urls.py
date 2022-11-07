from django.urls import path ,include
from django.contrib import admin
from userInquire import views

app_name = 'userInquire'

urlpatterns = [
    path('getList', views.getUserInquireList.as_view(), name='getUserInquireList'),
    path('create/<storage_key>', views.createInquire.as_view(), name='createInquire'),
    path('manage/<qid>', views.manageInquire.as_view(), name='manageInquire'),
    path('delete/<qid>', views.deleteInquire.as_view(), name='deleteInquire'),
]