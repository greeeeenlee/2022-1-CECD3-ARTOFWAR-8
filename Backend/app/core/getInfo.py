from .models import Userinfo, Videoinfo

def getUserInfo(userID):
    user = Userinfo.objects.get(ID = userID)
    result = {}
    result['name'] = user['name']
    result['id'] = user['id']

    return result

def getVideoInfo(userID, videoID):
    video = Videoinfo.objects.get(vid = videoID)
    result = {}
    result['name'] = video.name
    result['mjclass'] = video.mjclass
    result['subclass'] = video.subclass
    result['status'] = video.status
    result['introduction'] = video.introduction
    result['storage_key'] = video.storage_key
    result['storage_url'] = video.storage_url

    return result




