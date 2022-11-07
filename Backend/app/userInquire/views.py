from django.shortcuts import render
from django.http import JsonResponse
from rest_framework.views import APIView
import json
from .manageInquire import *
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_protect, ensure_csrf_cookie, csrf_exempt
from core.mixins import *
from core.auth import jwt_id

#   동영상 문의 작성
@method_decorator(ensure_csrf_cookie, name="dispatch")
class createInquire(PublicApiMixin,APIView):
    def post(self,request,storage_key):
        try:
            userID = jwt_id(request) # 헤더를 통해 userID 추출
            data=json.loads(json.dumps(request.data))
            inquireVideo(userID,storage_key, data) # 동영상 문의 작성
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

#   유저별 문의 리스트
@method_decorator(ensure_csrf_cookie, name="dispatch")
class getUserInquireList(PublicApiMixin,APIView):
    def get(self,request):
        try:
            userID = jwt_id(request)    # 헤더를 통해 userID 추출
            result=getInquireList(userID)   # 해당 유저의 문의 리스트 반환
            return JsonResponse({'message' : result}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

#   문의 내용 반환 & 문의 수정
@method_decorator(ensure_csrf_cookie, name="dispatch")
class manageInquire(PublicApiMixin,APIView):
    def get(self, request, qid):
        try:
            result= getInquireInfo(qid) # qid에 해당하는 문의 내용 반환
            return JsonResponse({'message' : result}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)

    def patch(self,request,qid):
        try:
            data = json.loads(request.body)
            updateInquire(qid, data)    # qid에 해당하는 문의 내용 수정
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

#   문의 삭제
@method_decorator(ensure_csrf_cookie, name="dispatch")
class deleteInquire(PublicApiMixin,APIView):
    def get(self, request, qid):
        try:
            deleteInquire(qid)  #qid에 해당하는 문의 삭제
            return JsonResponse({'message' : 'SUCCESS'}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)