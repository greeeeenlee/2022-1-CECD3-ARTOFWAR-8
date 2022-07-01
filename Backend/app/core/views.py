from django.shortcuts import render
from django.http import HttpResponse
# Create your views here.

from core import tasks


def my_pub_view(request):
    tasks.add(1,2)
    return HttpResponse(status=201)
