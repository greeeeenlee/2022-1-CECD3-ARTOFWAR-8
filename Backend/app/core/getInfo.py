from .models import Userinfo, Videoinfo

def getUserInfo(userID):
    user = Userinfo.objects.get(uid = userID)
    result = {}
    result['name'] = user['name']
    result['id'] = user['id']

    return result

def getVideoInfo(videoID):
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

def getVideoAddress(videoID):
    video = Videoinfo.objects.get(vid = videoID)
    result = video.storage_key

    return result

def updateVideoInfo(vid, data):
    video = Videoinfo.objects.filter(vid = vid)
    video.update(
        name=data['name'],
        introduction=data['introduction']
    )

def getVideoNum(userID):
    qs = Videoinfo.objects.filter(uid=userID)
    return qs.count()
