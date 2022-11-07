from django.http import HttpResponse, JsonResponse
from rest_framework.views import APIView
from drf_yasg.utils import swagger_auto_schema
from rest_framework.generics import GenericAPIView
from rest_framework import parsers, renderers
from rest_framework.response import Response
from django.core.files.storage import default_storage
from django.conf import settings
from collections import OrderedDict
from .models import Userinfo,Videoinfo
import json, bcrypt, jwt, re
from rest_framework import exceptions
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from django.contrib.auth.models import User
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_protect, ensure_csrf_cookie, csrf_exempt
from .mixins import *
from .auth import jwt_login,jwt_id

@method_decorator(ensure_csrf_cookie, name="dispatch")
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