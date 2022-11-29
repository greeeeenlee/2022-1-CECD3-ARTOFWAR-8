from django.urls import path ,include
from adminInquire import views

app_name = 'adminInquire'

urlpatterns = [
    path('nullQ', views.nullQuestion.as_view(), name='nullQuestion'),
    path('getList', views.getAdminInquireList.as_view(), name='getAdminInquireList'),
    path('manage/<qid>', views.manageAInquire.as_view(), name='manageAInquire'),
]