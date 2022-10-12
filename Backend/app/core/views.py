from django.http import HttpResponse, JsonResponse
from rest_framework.views import APIView
from drf_yasg.utils import swagger_auto_schema
from .serializer import *
from rest_framework.generics import GenericAPIView
from rest_framework import parsers, renderers
from rest_framework.response import Response
from django.core.files.storage import default_storage
from django.conf import settings
from collections import OrderedDict
from .models import Userinfo,Videoinfo
import json, bcrypt, jwt, re
from .tasks import *
from .getInfo import *
from .manageInquire import *
from rest_framework import exceptions
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from django.contrib.auth.models import User
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_protect, ensure_csrf_cookie
from .mixins import *
from .auth import jwt_login,jwt_id
from celery import chain

@method_decorator(ensure_csrf_cookie, name="dispatch")
class my_pub_view(PublicApiMixin,APIView):
    parser_classes = (parsers.FormParser, parsers.MultiPartParser, parsers.FileUploadParser)
    renderer_classes = (renderers.JSONRenderer,)
    serializer_class = testSerializer
    @swagger_auto_schema(tags=["테스트"],
                         request_body=testSerializer,
                         responses={
                             200: '성공',
                             400: '오류',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def post(self, request):
        try:
            if classify_video(request.POST['address'],request.POST['image'])==False:
                return JsonResponse({'message' : 'fail classify video'}, status=401)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)


@method_decorator(ensure_csrf_cookie, name="dispatch")
class uploadVideo(PublicApiMixin,GenericAPIView):
    parser_classes = (parsers.FormParser, parsers.MultiPartParser, parsers.FileUploadParser)
    renderer_classes = (renderers.JSONRenderer,)
    serializer_class = videoSerializer
    @swagger_auto_schema(tags=["동영상 업로드"],
                         request_body=videoSerializer,
                         responses={
                             200: '성공',
                             400: '실패',
                             401: '이미지 유해 판별 실패',
                             402: 'STT 변환 및 키워드 추출 실패',
                             500: '서버에러'
                         })
    def post(self, request):
        try: 
            info={}
            info['uid']=jwt_id(request)
            # 전달받은 동영상 정보 저장
            info['name'] = request.POST['name']
            info['mjclass'] = request.POST['mjclass']
            info['subclass'] = request.POST['subclass']
            info['address'] = request.POST['address']
            if request.POST['image']=='Y':
                    info['image']=1
            else:
                info['image']=0
            vid=store_video(info)     #동영상 정보 mysql 저장       
            # 유해 동영상 필터링 진행 및 추가 정보 업로드
            if classify_video(info['address'],info['image'])==False:
                return JsonResponse({'message' : 'fail classify video'}, status=401)
            #if analysis_vidseo(address,vid)==False:
                #return JsonResponse({'message' : 'fail analysis video'}, status=402)                  
            #default_storage.delete(file_name)
            #if image_file_name != 'None':
                #default_storage.delete(image_file_name)
        except:
            return JsonResponse({'message' : 'fail'}, status=400)
        
        return JsonResponse({'message' : 'SUCCESS'}, status=200)

@method_decorator(ensure_csrf_cookie, name="dispatch")
class signUp(PublicApiMixin,APIView):
    @swagger_auto_schema(tags=["회원 가입"],
                         request_body=signUpSerializer,
                         responses={
                             200: '성공',
                             400: '비밀번호 형식 오류',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def post(self, request):
        try:
            data = json.loads(request.body)
            ID = data['ID']
            pwd = data['pwd']
            name = data['name']
            regex_pwd = '\S{8,25}'
            if not re.match(regex_pwd, pwd):
                return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=400)
            pwd = data['pwd'].encode('utf-8')
            pwd_crypt = bcrypt.hashpw(pwd, bcrypt.gensalt()).decode('utf-8')
            
            Userinfo.objects.create(ID=ID, name=name, password=pwd_crypt)
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

@method_decorator(ensure_csrf_cookie, name="dispatch")
class checkID(PublicApiMixin,APIView):
    @swagger_auto_schema(tags=["아이디 중복 체크"],
                         responses={
                             200: '성공',
                             400: '아이디 중복',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request,uid):
        try:
            if Userinfo.objects.filter(ID=uid).exists():
                return JsonResponse({'message' : 'ALREADY_EXISTS'}, status=400)
            else:
                return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)
@method_decorator(ensure_csrf_cookie, name="dispatch")
class user(PublicApiMixin,APIView):
    @swagger_auto_schema(tags=["로그인"],
                         request_body=loginSerializer,
                         responses={
                             200: '성공',
                             400:'올바르지않은 아이디',
                             401:'올바르지않은 비밀번호',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def post(self, request,*args,**kwargs):
        try:
            data = json.loads(request.body)
            user = Userinfo.objects.get(ID = data['ID'])
            if not bcrypt.checkpw(data['pwd'].encode('utf-8'), user.password.encode('utf-8')):
                return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=401)
            response = Response(status=200)
            return jwt_login(response,user)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=400)

    @swagger_auto_schema(tags=["로그아웃"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request):
        try:
            response = JsonResponse({ "message": "Logout success"}, status=200)
            response.delete_cookie('refreshtoken')
            return response
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

@method_decorator(ensure_csrf_cookie, name="dispatch")
class userInfo(ApiAuthMixin,APIView):
    @swagger_auto_schema(tags=["비밀번호 변경"],
                         request_body=changePwdSerializer,
                         responses={
                             200: '성공',
                             400: '비밀번호 검증 실패',
                             500: '서버에러'
                         })
    def post(self, request,uid):
        data = json.loads(request.body)
        regex_pwd = '\S{8,25}'
        if not re.match(regex_pwd, data['pwd']):
            return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=400)
        pwd = data['pwd'].encode('utf-8')
        pwd_crypt = bcrypt.hashpw(pwd, bcrypt.gensalt()).decode('utf-8')
        Userinfo.objects.filter(ID = uid).update(password=pwd_crypt)
        return JsonResponse({'message' : 'SUCCESS'}, status=200)

class RefreshJWTtoken(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        refresh_token = request.COOKIES.get('refreshtoken')
        
        if refresh_token is None:
            return Response({
                "message": "Authentication credentials were not provided."
            }, status=status.HTTP_403_FORBIDDEN)
        
        try:
            payload = jwt.decode(
                refresh_token, settings.REFRESH_TOKEN_SECRET, algorithms=settings.ALGORITHM
            )
        except:
            return Response({
                "message": "expired refresh token, please login again."
            }, status=status.HTTP_403_FORBIDDEN)
        
        user = User.objects.filter(id=payload['user_id']).first()
        
        if user is None:
            return Response({
                "message": "user not found"
            }, status=status.HTTP_400_BAD_REQUEST)
        if not user.is_active:
            return Response({
                "message": "user is inactive"
            }, status=status.HTTP_400_BAD_REQUEST)
        
        access_token = generate_access_token(user)
        
        return Response(
            {
                'access_token': access_token,
            }
        )

@method_decorator(ensure_csrf_cookie, name="dispatch")
class videoInfo(ApiAuthMixin,APIView):
    @swagger_auto_schema(tags=["동영상 정보 요청"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request, vid):
        try:
            userID = jwt_id(request)
            result = getVideoInfo(vid)
            return JsonResponse(result, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

    @swagger_auto_schema(tags=["동영상 정보 수정"],
                         request_body=changeVideoInfoSerializer,
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def patch(self,request,vid):
        try:
            data = json.loads(request.body)
            updateVideoInfo(vid, data)
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)
@method_decorator(ensure_csrf_cookie, name="dispatch")
class category(ApiAuthMixin,APIView):
    @swagger_auto_schema(tags=["유저 이름 반환"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request):
        try:
            userID = jwt_id(request)
            result = getUserName(userID)
            return JsonResponse({'message' : result}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

@method_decorator(ensure_csrf_cookie, name="dispatch")
class userMain(ApiAuthMixin,APIView):
    @swagger_auto_schema(tags=["유저 별 동영상 개수"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request):
        try:
            userID = jwt_id(request)
            result = getVideoNum(userID)
            return JsonResponse({'message' : result}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

@method_decorator(ensure_csrf_cookie, name="dispatch")
class adminMain(SuperUserMixin,APIView):
    @swagger_auto_schema(tags=["답변하지않은 문의 개수"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request):
        try:
            result = nullQuestion()
            return JsonResponse({'message' : result}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

class deleteVideo(PublicApiMixin, APIView):
    @swagger_auto_schema(tags=["동영상 삭제 요청"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request, vid):
        try:
            storage_key=getVideoAddress(vid)
            delete_s3_video(storage_key)
            video = Videoinfo.objects.get(vid = vid)
            video.delete()
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

class createInquire(ApiAuthMixin,APIView):
    @swagger_auto_schema(tags=["동영상 문의 작성"],
                        request_body=inquireUserSerializer,
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def post(self,request):
        try:
            userID = jwt_id(request)
            data=json.loads(json.dumps(request.data))
            inquireVideo(userID, data)
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

class inquireUser(ApiAuthMixin,APIView):
    @swagger_auto_schema(tags=["문의 내용 확인"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request, qid):
        try:
            result= getInquireInfo(qid)
            
            return JsonResponse(result, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)

    @swagger_auto_schema(tags=["동영상 문의 수정"],
                         request_body=inquireUserSerializer,
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def patch(self,request,qid):
        try:
            data = json.loads(request.body)
            updateInquire(qid, data)
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)
class inquireAdmin(SuperUserMixin, APIView):
    @swagger_auto_schema(tags=["문의 내용 답변"],
                         request_body=inquireVideoSerializer,
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def patch(self, request,qid):
        try:
            data = json.loads(request.body)
            inquire = Inquire.objects.get(qid = qid)
            inquire.question=data['question']
            inquire.save()
            return JsonResponse({'message' : 'SUCCESS'}, status=201)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)
    @swagger_auto_schema(tags=["문의 내용 확인"],
                         responses={
                             200: '성공',
                             403: 'Key Error',
                             500: '서버에러'
                         })
    def get(self, request,qid):
        try:
            result= getInquireInfo(qid)
            
            return JsonResponse(result, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)