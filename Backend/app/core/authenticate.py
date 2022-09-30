from rest_framework import exceptions
from rest_framework.authentication import BaseAuthentication, CSRFCheck

from django.conf import settings
from django.contrib.auth import get_user_model
from .models import Userinfo
import json, bcrypt, jwt, re

User = get_user_model()


class JWTAuthentication(BaseAuthentication):
    
    def authenticate(self, request):
        authorization_header = request.headers.get('Authorization')
        
        if authorization_header == None:
            return None
            
        try:
            prefix = authorization_header.split(' ')[0]
            if prefix.lower() != 'jwt':
                raise exceptions.AuthenticationFailed('Token is not jwt')

            access_token = authorization_header.split(' ')[1]
            payload = jwt.decode(
                access_token, settings.SECRET_KEY, algorithms=settings.ALGORITHM
            )
        except jwt.ExpiredSignatureError:
            raise exceptions.AuthenticationFailed('access_token expired')
        except IndexError:
            raise exceptions.AuthenticationFailed('Token prefix missing')
        
        return self.authenticate_credentials(request, payload['uid'])
    
    def authenticate_credentials(self, request, key):
        user = Userinfo.objects.filter(uid=key).first()
        
        if user is None:
            raise exceptions.AuthenticationFailed('User not found')
        
        self.enforce_csrf(request)
        return (user, None)

    def enforce_csrf(self, request):
        check = CSRFCheck()
        
        check.process_request(request)
        reason = check.process_view(request, None, (), {})
        if reason:
            raise exceptions.PermissionDenied(f'CSRF Failed: {reason}')

class AdministratorAuthentication(BaseAuthentication):
    def authenticate(self, request):
        authorization_header = request.headers.get('Authorization')
        
        if authorization_header == None:
            return None
            
        try:
            prefix = authorization_header.split(' ')[0]
            if prefix.lower() != 'jwt':
                raise exceptions.AuthenticationFailed('Token is not jwt')

            access_token = authorization_header.split(' ')[1]
            payload = jwt.decode(
                access_token, settings.SECRET_KEY, algorithms=settings.ALGORITHM
            )
        except jwt.ExpiredSignatureError:
            raise exceptions.AuthenticationFailed('access_token expired')
        except IndexError:
            raise exceptions.AuthenticationFailed('Token prefix missing')
        
        return self.authenticate_credentials(request, payload['uid'])
    
    def authenticate_credentials(self, request, key):
        admin = Userinfo.objects.filter(uid=key).first()
        
        if admin is None:
            raise exceptions.AuthenticationFailed('Admin not found')
        if admin.is_staff != 1:
            raise exceptions.AuthenticationFailed('Admin Login Failed')

        self.enforce_csrf(request)
        return (admin, None)

    def enforce_csrf(self, request):
        check = CSRFCheck()
        
        check.process_request(request)
        reason = check.process_view(request, None, (), {})
        if reason:
            raise exceptions.PermissionDenied(f'CSRF Failed: {reason}')
