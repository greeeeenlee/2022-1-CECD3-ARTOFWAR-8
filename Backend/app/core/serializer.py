from rest_framework import serializers

class videoSerializer(serializers.Serializer):
    videoFile = serializers.FileField()
    name = serializers.CharField()

class loginSerializer(serializers.Serializer):
    ID = serializers.CharField()
    pwd = serializers.CharField()

class signUpSerializer(serializers.Serializer):
    ID = serializers.CharField()
    pwd = serializers.CharField()
    name = serializers.CharField()
