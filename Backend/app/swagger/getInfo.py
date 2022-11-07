from core.models import Userinfo, Videoinfo

def getUserInfo(userID):
    user = Userinfo.objects.get(uid = userID)
    result = {}
    result['name'] = user.name
    result['id'] = user.ID

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

def getVideoList(userID):
    video = Videoinfo.objects.filter(uid = userID)

    resultList = {}
    count=0
    for i in video:
        result={}
        result['name'] = i.name
        result['upload_time'] = i.upload_time
        result['mjclass'] = i.mjclass
        result['subclass'] = i.subclass
        result['status'] = i.status
        result['status_detail'] = i.status_detail
        result['image_ext'] = i.image_ext
        result['introduction'] = i.introduction
        result['storage_key'] = i.storage_key
        resultList[count]=(result)
        count+=1

    return resultList

def updateVideoInfo(storage_key,data):
    video = Videoinfo.objects.filter(storage_key = storage_key)
    video.update(
        name=data['name'],
        introduction=data['introduction']
    )

def getVideoNum(userID):
    qs = Videoinfo.objects.filter(uid=userID)
    return qs.count()