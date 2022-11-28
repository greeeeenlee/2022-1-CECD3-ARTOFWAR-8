from core.models import Userinfo, Videoinfo
from django.conf import settings

#   동영상 정보 DB 저장
def store_video(info):
    user=Userinfo.objects.get(uid=info['uid']) # uid에 해당하는 유저 데이터 DB에서 추출
    url = "/".join([settings.BUCKET_URL,info['address']])   # s3 스토리지에 해당하는 동영상 url 생성
    Videoinfo.objects.create(name=info['name'], storage_url=url, image=info['image'],image_ext=info['image_ext'],storage_key=info['address'],
            mjclass=info['mjclass'],subclass=info['subclass'],status=3, #상태 판단 중
            uid=user) # 유저-동영상 정보 DB에 저장

#   동영상 정보 DB 삭제
def delete_video(storage_key):
    video = Videoinfo.objects.get(storage_key = storage_key) # storage_key에 해당하는 동영상 데이터 DB에서 추출
    video.delete() # 데이터 삭제

#   유저별 동영상 정보 리스트 반환
def getVideoList(userID):
    video = Videoinfo.objects.filter(uid = userID) # uid에 해당하는 유저의 동영상 데이터 DB에서 추출

    resultList = {}
    count=0
    for i in video: # 데이터를 json 형식으로 변환
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
        result['storage_url'] = i.storage_url
        resultList[count]=(result)
        count+=1

    return resultList

#   동영상 정보 수정
def updateVideoInfo(storage_key,data):
    video = Videoinfo.objects.filter(storage_key = storage_key)# storage_key에 해당하는 동영상 데이터 DB에서 추출
    video.update(   # 해당 데이터 정보 수정
        name=data['name'],
        introduction=data['introduction']
    )

#   동영상 개수 반환
def getVideoNum(userID):
    qs = Videoinfo.objects.filter(uid=userID) # uid에 해당하는 유저의 동영상 데이터 DB에서 추출
    return qs.count()   # 동영상 개수 반환


#   동영상 썸네일 여부 반환
def getImage(storage_key):
    video = Videoinfo.objects.get(storage_key=storage_key) # storage_key에 해당하는 유저의 동영상 데이터 DB에서 추출
    return video.name   # 동영상 썸네일 여부 반환
   