from rest_framework.permissions import IsAuthenticated

from .authenticate import JWTAuthentication,AdministratorAuthentication


class ApiAuthMixin:
    authentication_classes = (JWTAuthentication, )
    permission_classes = (IsAuthenticated, )

class SuperUserMixin:
    authentication_classes = (AdministratorAuthentication, )
    permission_classes = (IsAuthenticated, )

class PublicApiMixin:
    authentication_classes = ()
    permission_classes = ()