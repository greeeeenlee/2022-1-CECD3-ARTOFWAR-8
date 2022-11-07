from core.models import Inquire

#   문의 내용 확인을 위해 추출
def getInquireInfo(qid):    
    inquire = Inquire.objects.get(qid=qid)  # 전달받은 qid를 통해 문의 DB에서 추출
    
    result = {}
    result['name'] = inquire.name
    result['video_name']=inquire.vid.name
    result['content'] = inquire.content
    result['question'] = inquire.question

    return result

#   답변이 필요한 문의 리스트 
def getInquireList():
    inquire = Inquire.objects.filter(question__isnull=True) # 답변이 null인 문의 DB에서 모두 추출

    resultList = {}
    count=0
    for i in inquire:   # 모든 문의 json 형식으로 변환
        result={}
        result['qid'] = i.qid
        result['name'] = i.name
        result['content'] = i.content
        result['question'] = i.question
        resultList[count]=(result)
        count+=1

    return resultList

#   관리자의 답변 반영
def answerInquire(qid):
    inquire = Inquire.objects.get(qid = qid) # 전달받은 qid를 통해 문의 DB에서 추출
    inquire.question=data['question'] # 해당 데이터 답변 반영
    inquire.save()

#   답변하지않은 문의 개수 
def nullAdminQuestion():
    nullq=Inquire.objects.filter(question__isnull=True) # 답변이 null인 문의 DB에서 모두 추출
    return str(nullq.count()) # 개수 전달