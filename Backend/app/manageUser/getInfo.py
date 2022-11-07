from core.models import Userinfo, Videoinfo
from django.http import JsonResponse
from core.auth import jwt_login
import json,bcrypt, jwt, re
from rest_framework.response import Response

#   전달받은 데이터로 회원가입
def userSignUp(data):
    if Userinfo.objects.filter(ID= data['ID']).exists(): # ID 중복 확인
        return JsonResponse({'message' : 'ALREADY_EXISTS'}, status=400) # 중복일 경우, 오류 반환
    pwd = data['pwd']
    name = data['name']
    regex_pwd = '\S{8,25}'
    if not re.match(regex_pwd, pwd):    # 비밀번호가 형식에 맞는지 확인
        return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=400)  # 맞지않을 경우, 오류 반환
    
    pwd = data['pwd'].encode('utf-8')   
    pwd_crypt = bcrypt.hashpw(pwd, bcrypt.gensalt()).decode('utf-8') # 비밀번호 암호화
            
    Userinfo.objects.create(ID=ID, name=name, password=pwd_crypt) # DB에 유저 정보 추가 - 회원 가입
    return JsonResponse({'message' : 'SUCCESS'}, status=200)

#   ID 중복 체크
def checkUserID(uid):
    result=Userinfo.objects.filter(ID=uid).exists() # 유저 ID와 동일한 데이터 DB에서 추출
    return result

#   로그인
def login(data):
    try:
        user = Userinfo.objects.get(ID = data['ID'])    # 유저 ID에 맞는 데이터 DB에서 추출
        if not bcrypt.checkpw(data['pwd'].encode('utf-8'), user.password.encode('utf-8')):  # 비밀번호 확인
            return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=401) # 비밀번호 틀릴 경우, 오류 반환
        response = Response(status=200) 
        return jwt_login(response,user) # jwt_login 진행 - access_token과 refresh_token 생성
    except Userinfo.DoesNotExist:   
            return JsonResponse({'message' : 'INVALID_USER'}, status=400)

#   비밀번호 변경
def changeUserPwd(userID,data):
    regex_pwd = '\S{8,25}'
    if not re.match(regex_pwd, data['pwd']):
        return JsonResponse({'message' : 'INVALID_PASSWORD'}, status=400)
    pwd = data['pwd'].encode('utf-8')
    pwd_crypt = bcrypt.hashpw(pwd, bcrypt.gensalt()).decode('utf-8')
    Userinfo.objects.filter(uid = userID).update(password=pwd_crypt)

    return JsonResponse({'message' : 'SUCCESS'}, status=200)

#   유저 정보 반환
def getUserInfo(userID):
    user = Userinfo.objects.get(uid = userID) # 유저 ID에 맞는 데이터 DB에서 추출

    result = {}
    result['name'] = user.name
    result['id'] = user.ID

    return result