from rest_framework import serializers

class videoSerializer(serializers.Serializer):
    videoFile = serializers.FileField()
    name = serializers.CharField()