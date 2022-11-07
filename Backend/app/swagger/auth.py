import datetime, jwt

from django.conf import settings


def generate_access_token(user):
    access_token_payload = {
        'uid': user.uid,
        'exp': datetime.datetime.utcnow() + datetime.timedelta(
            days=0, minutes=60
        ),
        'iat': datetime.datetime.utcnow(),
    }
    
    access_token = jwt.encode(
        access_token_payload,
        settings.SECRET_KEY, algorithm=settings.ALGORITHM 
    )
    
    return access_token.encode().decode()
    
    
def generate_refresh_token(user):
    refresh_token_payload = {
        'uid': user.uid,
        'exp': datetime.datetime.utcnow() + datetime.timedelta(days=7),
        'iat': datetime.datetime.utcnow(),
    }
    
    refresh_token = jwt.encode(
        refresh_token_payload,
        settings.SECRET_KEY, algorithm=settings.ALGORITHM
    )
    
    return refresh_token.encode().decode()


def jwt_login(response, user):
    access_token = generate_access_token(user)
    refresh_token = generate_refresh_token(user)
    
    data = {
        'access_token': access_token,
    }
    
    response.data = data
    response.set_cookie(key="refreshtoken", value=refresh_token, httponly=True)
    
    return response

def jwt_id(request):
    authorization_header = request.headers.get('Authorization')
    access_token = authorization_header.split(' ')[1]
    payload = jwt.decode(access_token, settings.SECRET_KEY, algorithms=settings.ALGORITHM)
    
    return payload['uid']