from django.http import HttpResponse, JsonResponse
from rest_framework.views import APIView
from drf_yasg.utils import swagger_auto_schema
from .serializer import videoSerializer, loginSerializer, signUpSerializer, changePwdSerializer
from rest_framework.generics import GenericAPIView
from rest_framework import parsers, renderers
from django.core.files.storage import default_storage
from django.conf import settings
from .models import Userinfo
import json, bcrypt, jwt, re
from .tasks import add, upload_video,classify_video,analysis_video
from .getInfo import getUserInfo, getVideoInfo


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
            storage_key=upload_video(file_name, name)
            
            # 유해 동영상 필터링 진행 및 추가 정보 업로드
            classify_video(storage_key)
            analysis_video(storage_key)

        except:
            return HttpResponse(status=400)
        
        return HttpResponse(status=200)

class signUp(APIView):
    @swagger_auto_schema(tags=["회원 가입"],
                         request_body=signUpSerializer,
                         responses={
                             200: '성공',
                             400: '비밀번호 검증 실패',
                             401: '존재 하지 않는 사용자',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def post(self, request):
        try:
            data = json.loads(request.body)
            ID = data['ID']
            pwd = data['pwd']
            name = data['name']
            if Userinfo.objects.filter(ID=ID).exists():
                return JsonResponse({'message' : 'ALREADY_EXISTS'}, status=400)
            regex_pwd = '\S{8,25}'
            if not re.match(regex_pwd, pwd):
                return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=400)
            pwd = data['pwd'].encode('utf-8')
            pwd_crypt = bcrypt.hashpw(pwd, bcrypt.gensalt()).decode('utf-8')
            
            Userinfo.objects.create(ID=ID, name=name, password=pwd_crypt)
            return JsonResponse({'message' : 'SUCCESS'}, status=201)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)

class user(APIView):
    @swagger_auto_schema(tags=["로그인"],
                         request_body=loginSerializer,
                         responses={
                             200: '성공',
                             400: '비밀번호 검증 실패',
                             401: '존재 하지 않는 사용자',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def post(self, request):
        try:
            data = json.loads(request.body)
            user = Userinfo.objects.get(ID = data['ID'])
            if not bcrypt.checkpw(data['pwd'].encode('utf-8'), user.password.encode('utf-8')):
                return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=400)
            request.session['userID'] = data['ID']
            token = jwt.encode({'password' : data['pwd']}, settings.SECRET_KEY, algorithm=settings.ALGORITHM)
            return JsonResponse({'message' : 'SUCCESS', 'access_token': token.encode().decode()}, status=200)

        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

    @swagger_auto_schema(tags=["로그아웃"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request):
        try:
            request.session.pop('userID', None)
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

    @swagger_auto_schema(tags=["비밀번호 변경"],
                         request_body=changePwdSerializer,
                         responses={
                             200: '성공',
                             400: '비밀번호 검증 실패',
                             500: '서버에러'
                         })
    def put(self, request):
        data = json.loads(request.body)
        regex_pwd = '\S{8,25}'
        if not re.match(regex_pwd, data['pwd']):
            return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=400)
        pwd = data['pwd'].encode('utf-8')
        pwd_crypt = bcrypt.hashpw(pwd, bcrypt.gensalt()).decode('utf-8')
        Userinfo.objects.filter(ID = request.session['userID']).update(password=pwd_crypt)
        return JsonResponse({'message' : 'SUCCESS'}, status=200)

class videoInfo(APIView):
    @swagger_auto_schema(tags=["동영상 정보 요청"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request, vid):
        try:
            userID = request.session['userID']
            result = getVideoInfo(userID, vid)
            return JsonResponse(result, status=200)

        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)
