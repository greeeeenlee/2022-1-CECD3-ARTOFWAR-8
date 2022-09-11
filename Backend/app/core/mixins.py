from rest_framework.permissions import IsAuthenticated

from .authenticate import JWTAuthentication


class ApiAuthMixin:
    authentication_classes = (JWTAuthentication, )
    permission_classes = (IsAuthenticated, )

class SuperUserMixin:
    #authentication_classes = (AdministratorAuthentication, )
    authentication_classes = ()
    permission_classes = (IsAuthenticated, )

class PublicApiMixin:
    authentication_classes = ()
    permission_classes = ()