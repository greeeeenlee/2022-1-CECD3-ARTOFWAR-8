from .models import Userinfo, Videoinfo,Inquire

def inquireVideo(uid,userInquire):
    user=Userinfo.objects.get(uid=uid) 
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
    inquire = Inquire.objects.get(qid = qid)
    inquire.update(
                name=data['name'],
                content=data['content']
            )

def nullQuestion():
    nullq=Inquire.objects.filter(question__isnull=True)
    return nullq.count()
