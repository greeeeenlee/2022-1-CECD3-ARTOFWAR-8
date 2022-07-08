from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from rest_framework.views import APIView
from drf_yasg.utils import swagger_auto_schema
from drf_yasg import openapi
from .serializer import videoSerializer
from rest_framework.generics import GenericAPIView
from rest_framework import parsers, renderers, serializers, status
from django.core.files.storage import default_storage
import random
from datetime import datetime
# Create your views here.

from .tasks import add, upload_video


class my_pub_view(APIView):
    @swagger_auto_schema(tags=["테스트"],
                         responses={
                             200: "성공",
                             403: '인증에러',
                             400: '입력값 유효성 검증 실패',
                             500: '서버에러'
                         })
    def get(self,request):
        add.delay(1,2)
        return HttpResponse(status=200)

class uploadVideo(GenericAPIView):
    parser_classes = (parsers.FormParser, parsers.MultiPartParser, parsers.FileUploadParser)
    renderer_classes = (renderers.JSONRenderer,)
    serializer_class = videoSerializer
    def post(self, request):
        try:
            # 입력된 동영상과 정보 업로드
            video = request.data.get('videoFile',None)
            file_name = default_storage.save(video.name, video)
            
            name = request.data.get('name',None)
            upload_video.delay(file_name, name)
            
            # 유해 동영상 필터링 진행 및 추가 정보 업로드


        except:
            return HttpResponse(status=400)
        
        return HttpResponse(status=200)


