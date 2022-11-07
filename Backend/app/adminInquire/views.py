from django.shortcuts import render
from django.http import JsonResponse
from rest_framework.views import APIView
import json
from .manageInquire import *
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_protect, ensure_csrf_cookie, csrf_exempt
from core.mixins import *
from core.auth import jwt_id

#   관리자의 답변이 필요한 문의 리스트
@method_decorator(ensure_csrf_cookie, name="dispatch")
class getAdminInquireList(SuperUserMixin,APIView):
    def get(self,request):
        try:
            result=getInquireList()     # 답변이 필요한 문의 리스트 
            return JsonResponse({'message' : result}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)

#   문의 내용 답변 & 문의 내용 확인
@method_decorator(ensure_csrf_cookie, name="dispatch")
class manageAInquire(SuperUserMixin, APIView):
    def patch(self, request,qid):
        try:
            data = json.loads(request.body)
            answerInquire(qid)  #   qid에 해당하는 문의 내용 답변 DB 반영
            return JsonResponse({'message' : 'SUCCESS'}, status=201)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)
    def get(self, request,qid):
        try:
            result= getInquireInfo(qid)     # qid에 해당하는 문의 내용 전달
            return JsonResponse({'message' : result}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=400)

#   답변하지않은 문의 개수
@method_decorator(ensure_csrf_cookie, name="dispatch")
class nullQuestion(SuperUserMixin,APIView):
    def get(self, request):
        try:
            result = nullAdminQuestion()    # 답변하지않은 문의 개수 반환
            return JsonResponse({'message' : result}, status=200)
        except KeyError:
            return JsonResponse({'message' : 'KEY_ERROR'}, status=403)
