from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from rest_framework.response import Response
from rest_framework.views import APIView
import json
from .getInfo import *
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_protect, ensure_csrf_cookie, csrf_exempt
from core.mixins import *
from core.auth import jwt_id

#   회원 가입
@method_decorator(ensure_csrf_cookie, name="dispatch")
class signUp(PublicApiMixin,APIView):
    def post(self, request):
        try:
            data = json.loads(request.body)
            result=userSignUp(data)
            return result
        except KeyError:
            return JsonResponse({'message' : result}, status=403)

#   ID 중복 체크
@method_decorator(ensure_csrf_cookie, name="dispatch")
class checkID(PublicApiMixin,APIView):
    def get(self, request,uid):
        try:
            if checkUserID(uid): # 중복이 있을 경우, 오류 반환
                return JsonResponse({'message' : 'ALREADY_EXISTS'}, status=400)
            else:   # 중복이 없을 경우, 성공 반환
                return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

#   로그인 & 로그아웃
@method_decorator(ensure_csrf_cookie, name="dispatch")
class log(PublicApiMixin,APIView):
    def post(self, request,*args,**kwargs):
        try:
            data = json.loads(request.body)
            result=login(data)  # 로그인
            return result
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

    def get(self, request):
        try:
            response = JsonResponse({ "message": "Logout success"}, status=200)
            response.delete_cookie('refreshtoken')  #cookie의 refresh_token을 삭제 - 로그아웃
            return response
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

#   비밀번호 변경
@method_decorator(ensure_csrf_cookie, name="dispatch")
class changepwd(PublicApiMixin,APIView):
    def post(self, request,*args,**kwargs):
        try:
            userID = jwt_id(request)    # 해당 유저 ID 헤더에서 추출s
            data = json.loads(request.body)
            result=changeUserPwd(userID,data)
            return result
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)

#   유저 정보 반환
@method_decorator(ensure_csrf_cookie, name="dispatch")
class getInfo(ApiAuthMixin,APIView):
    def get(self, request):
        try:
            userID = jwt_id(request)    # 해당 유저 ID 헤더에서 추출
            result = getUserInfo(userID)
            return JsonResponse({'message' : result}, status=200)
        except Userinfo.DoesNotExist:
            return JsonResponse({'message' : 'INVALID_USER'}, status=401)

#access token 추가 발급
@method_decorator(ensure_csrf_cookie, name="dispatch")
class RefreshJWTtoken(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        refresh_token = request.COOKIES.get('refreshtoken') # 쿠키에서 refresh token을 추출
        
        if refresh_token is None:
            return JsonResponse({"message": "Authentication credentials were not provided." }, status=403)
        
        try:
            payload = jwt.decode(refresh_token, settings.REFRESH_TOKEN_SECRET, algorithms=settings.ALGORITHM)  #refresh_token decode
        except:
            return JsonResponse({"message": "expired refresh token, please login again."}, status=403)
        
        user = Userinfo.objects.filter(uid=payload['uid']).first() #유저 DB에서 uid에 맞는 데이터 추출
        
        if user is None: # 유저 데이터가 존재하지않을 경우 오류 발생
            return JsonResponse({"message": "user not found" }, status=400)
        
        access_token = generate_access_token(user) # access_token 새로 발급
        
        return JsonResponse( {'access_token': access_token } ) #access_token 전달