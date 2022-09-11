from .models import Userinfo, Videoinfo,Inquire

def inquireVideo(uid,userInquire):
    user=Userinfo.objects.get(ID=uid) 
    video=Videoinfo.objects.get(vid=userInquire['vid']) 
    Inquire.objects.create(name=userInquire['name'], content=userInquire['content'], vid=video,uid=user)

def getInquireInfo(qid):
    inquire = Inquire.objects.get(qid=qid)
    result = {}
    result['name'] = inquire.name
    result['content'] = inquire.content
    result['question'] = inquire.question

    return result

def updateInquire(qid,data):
    record = Inquire.objects.filter(qid = qid)
    record.update(
                name=data['name'],
                content=data['content']
            )
