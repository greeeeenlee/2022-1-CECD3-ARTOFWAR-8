from django.urls import path

from core import views

app_name = 'api'

urlpatterns = [
    path('publish', views.my_pub_view.as_view(), name='publish'),
    path('upload', views.uploadVideo.as_view(), name='uploadVideo')
]