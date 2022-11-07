from django.http import HttpResponse, JsonResponse
from rest_framework.views import APIView
from rest_framework.generics import GenericAPIView
from rest_framework import parsers, renderers
from core.models import Userinfo
import json
from .tasks import *
from .getInfo import *
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_protect, ensure_csrf_cookie, csrf_exempt
from core.mixins import *
from core.auth import jwt_id

#동영상 업로드
@method_decorator(ensure_csrf_cookie, name="dispatch")
class uploadVideo(PublicApiMixin,GenericAPIView):
    parser_classes = (parsers.FormParser, parsers.MultiPartParser, parsers.FileUploadParser)
    renderer_classes = (renderers.JSONRenderer,)
    def post(self, request):
            info={}
            info['uid']=jwt_id(request) # 헤더에서 userID 추출
            # 전달받은 동영상 정보 json 형식으로 저장
            info['name'] = (request.POST['name']).strip("\"")
            info['mjclass'] = (request.POST['mjclass']).strip("\"")
            info['subclass'] = (request.POST['subclass']).strip("\"")
            info['address'] = (request.POST['address']).strip("\"")
            info['image_ext'] = (request.POST['image_ext']).strip("\"")
            if request.POST['image']=='Y':  # 동영상 업로드 시, 썸네일 이미지 여부
                    info['image']=1 # 이미지 있음
            else:   
                info['image']=0 # 이미지 없음

            store_video(info)     #동영상 정보 mysql 저장  
            
            # 유해 동영상 필터링 진행 및 추가 정보 업로드 - 비동기 처리
            #classify_video.delay(info['address'],info['image'])   # 유해 동영상 필터링 - 이미지 진행
            #analysis_video.delay(info['address'])   # 유해 동영상 필터링 - 음성 진행 + 주요 단어 키워드와 소개문 작성
            return JsonResponse({'message' : 'SUCCESS'}, status=200)

#동영상 삭제
@method_decorator(ensure_csrf_cookie, name="dispatch")
class deleteVideo(PublicApiMixin, APIView):
    def get(self, request, storage_key):
        try:
            delete_s3_video.delay(storage_key) # S3 스토리지 동영상 삭제 - 비동기 처리
            delete_video(storage_key)   #   DB 동영상 정보 삭제
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

#동영상 정보 리스트 반환
@method_decorator(ensure_csrf_cookie, name="dispatch")
class getInfoList(PublicApiMixin,APIView):
    def get(self, request):
        try:
            userID = jwt_id(request) # 헤더에서 userID 추출 
            result = getVideoList(userID)   # 유저별 동영상 정보 리스트 반환
            return JsonResponse({'message' : result}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

#동영상 정보 수정
@method_decorator(ensure_csrf_cookie, name="dispatch")
class changeInfo(PublicApiMixin,APIView):
    def post(self,request,storage_key):
        try:
            data = json.loads(request.body)
            updateVideoInfo(storage_key,data)   # 동영상 정보 수정
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

#유저별 동영상 개수 반환
@method_decorator(ensure_csrf_cookie, name="dispatch")
class getNum(PublicApiMixin,APIView):
    def get(self, request):
        try:
            userID = jwt_id(request)    # 헤더에서 userID 추출 
            result = getVideoNum(userID)    # 해당 유저의 동영상 개수 반환
            return JsonResponse({'message' : result}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)