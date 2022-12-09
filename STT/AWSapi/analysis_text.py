# -*- coding: utf-8 -*-
#pip install konlpy
#pip install Counter
from konlpy.tag import Okt
from collections import Counter
from gensim.summarization.summarizer import summarize
import json
from collections import OrderedDict
okt=Okt()#Okt 클래스 객체 만들기
json_data = OrderedDict()#텍스트 속 유해성 여부와 키워드 추출 json 
def analysisVIDText(text):
    #문장에서 i사만 추출
    target_noun=okt.nouns(text)## Okt 형태소 분석기를 통해 문장 안에있는 명사들을 추출
    analysisHarmful(target_noun)#명사 속 유해 단어 포함 여부 판단
    analysisKeywords(target_noun,text)#명사 속 키워드 추출
    return json_data

#명사 속 유해 단어 포함 여부 판단 함수
def analysisHarmful(target_noun):
    #비속어 리스트가 들어있는 파일 읽기
    f=open("./fword_list.txt","r",encoding='cp949')

    badwords=f.read()
    badword=badwords.splitlines() #비속어 목록을 스트링에서 리스트형으로 변경
    f.close()
        #비속어가 들어있는지 판단
    is_toxic=False
    for item in badword:    
        if item in target_noun:
          is_toxic=True

    if is_toxic:
      json_data["harmful"]=1
    else:
      json_data["harmful"]=0

#문장 속 키워드 추출 함수
def analysisKeywords(target_noun, text):
    count=Counter(target_noun)
    #i사형태 중에서 빈도수 많은 단어를 체크
    noun_list=count.most_common(10) #상위 10개의 단어
    #print(noun_list)
    #문장을 요약할 ratio 비율 구하기
    #문장 개수를 통해 ratio는 문장 개수의 1%로 설정
    count_sen=text.count('.')
    jari=int(len(str(count_sen)))
    ratios=round(0.1**(jari-1),jari)

    sentence1=summarize(text,ratio=ratios)
    test_noun=okt.nouns(sentence1)

    #명사 추출에 대한 빈도수 높은 단어 && 요약된 문장에서 추출된 명사 && 한글자로 구성된 단어는 제외하기
    keywords=[]
    for i,j in noun_list:
      if len(i)>1:
        if i in test_noun:
            keywords.append(i)
    json_data["keywords"]=keywords
    print(keywords)
