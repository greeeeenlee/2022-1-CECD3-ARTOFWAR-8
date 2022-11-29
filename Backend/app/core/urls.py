from django.urls import path ,include

app_name = 'core'

urlpatterns = [
     path('video/', include('video.urls')),       # 동영상 관리
     path('user/', include('manageUser.urls')),   # 유저 정보 관리
     path('uInquire/', include('userInquire.urls')),  # 유저 문의 관리
     path('aInquire/', include('adminInquire.urls')), # 관리자 문의 관리
]