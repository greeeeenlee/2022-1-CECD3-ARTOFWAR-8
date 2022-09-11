from rest_framework import serializers

class videoSerializer(serializers.Serializer):
    videoFile = serializers.FileField()
    imageFile = serializers.FileField(required=False)
    name = serializers.CharField()
    mjclass=serializers.CharField()
    subclass=serializers.CharField()

class loginSerializer(serializers.Serializer):
    ID = serializers.CharField()
    pwd = serializers.CharField()

class signUpSerializer(serializers.Serializer):
    ID = serializers.CharField()
    pwd = serializers.CharField()
    name = serializers.CharField()

class changePwdSerializer(serializers.Serializer):
    pwd = serializers.CharField()

class inquireUserSerializer(serializers.Serializer):
    vid=serializers.IntegerField()
    name = serializers.CharField()
    content = serializers.CharField()

class inquireVideoSerializer(serializers.Serializer):
    question = serializers.CharField()

class changeVideoInfoSerializer(serializers.Serializer):
    name = serializers.CharField()
    introduction=serializers.CharField()

class UserTestSerializer(serializers.Serializer):
    id = serializers.CharField()
    password=serializers.CharField()
    nickname=serializers.CharField()

