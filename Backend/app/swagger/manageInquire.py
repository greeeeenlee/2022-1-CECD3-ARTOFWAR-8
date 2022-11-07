from core.models import Userinfo, Videoinfo,Inquire

def inquireVideo(uid,storage_key,userInquire):
    user=Userinfo.objects.get(uid=uid) 
    video=Videoinfo.objects.get(storage_key=storage_key) 
    Inquire.objects.create(name=userInquire['name'], content=userInquire['content'], vid=video,uid=user)

def getInquireInfo(qid):
    inquire = Inquire.objects.get(qid=qid)
    
    result = {}
    result['name'] = inquire.name
    result['video_name']=inquire.vid.name
    result['content'] = inquire.content
    result['question'] = inquire.question

    return result

def updateInquire(qid,data):
    inq = Inquire.objects.filter(qid = qid)
    inq.update(
                name=data['name'],
                content=data['content']
            )

def nullQuestion():
    nullq=Inquire.objects.filter(question__isnull=True)
    return nullq.count()

def getInquireList(userID):
    inquire = Inquire.objects.filter(uid = userID)

    resultList = {}
    count=0
    for i in inquire:
        result={}
        result['qid'] = i.qid
        result['name'] = i.name
        result['content'] = i.content
        result['question'] = i.question
        resultList[count]=(result)
        count+=1

    return resultList

def getAdminInquireList():
    inquire = Inquire.objects.filter(question__isnull=True)

    resultList = {}
    count=0
    for i in inquire:
        result={}
        result['qid'] = i.qid
        result['name'] = i.name
        result['content'] = i.content
        result['question'] = i.question
        resultList[count]=(result)
        count+=1

    return resultList