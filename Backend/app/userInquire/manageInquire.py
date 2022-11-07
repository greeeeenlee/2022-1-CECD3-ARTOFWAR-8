from core.models import Userinfo, Videoinfo,Inquire

#   동영상 문의 작성
def inquireVideo(userID,storage_key,userInquire):
    user=Userinfo.objects.get(uid=userID)  # userID에 해당하는 데이터 DB에서 추출
    video=Videoinfo.objects.get(storage_key=storage_key) # storage_key에 해당하는 데이터 DB에서 추출
    Inquire.objects.create(name=userInquire['name'], 
            content=userInquire['content'], vid=video,uid=user) # 유저-동영상 데이터 포함 문의 작성

#   유저별 문의 리스트 반환
def getInquireList(userID):
    inquire = Inquire.objects.filter(uid = userID) # userID에 해당하는 데이터 DB에서 추출

    resultList = {}
    count=0
    for i in inquire:   # 데이터를 json 형식으로 변환
        result={}
        result['qid'] = i.qid
        result['name'] = i.name
        result['content'] = i.content
        result['question'] = i.question
        resultList[count]=(result)
        count+=1

    return resultList

#   문의 내용 반환
def getInquireInfo(qid):
    inquire = Inquire.objects.get(qid=qid) # qid에 해당하는 데이터 DB에서 추출
    
    result = {}
    result['name'] = inquire.name
    result['video_name']=inquire.vid.name
    result['content'] = inquire.content
    result['question'] = inquire.question

    return result

#   문의 내용 수정
def updateInquire(qid,data):
    inq = Inquire.objects.filter(qid = qid) # qid에 해당하는 데이터 DB에서 추출
    inq.update(
                name=data['name'],
                content=data['content']
            )   #문의 내용 수정

# 문의 삭제
def deleteInquire(qid):
    inquire = Inquire.objects.get(qid = qid) # qid에 해당하는 문의 DB에서 추출
    inquire.delete() # 해당 문의 삭제
