from urllib import request
from ast import literal_eval
import boto3
from botocore.config import Config
from analysis_text import analysisVIDText

bucket = 'cecd3'

def transcribe(vid_title):
    file_nm = vid_title

    # Transcribe를 위한 Config 설정
    my_config = Config(
    	region_name = 'us-east-1',
        signature_version = 'v4',
        retries={
        	'max_attempts':5,
            'mode':'standard'
        	}
        )

    # Transcribe 실행
    transcribe =boto3.client('transcribe', config=my_config)
    # s3에 업로드한 파일 URL
    job_uri = 'https://{}.s3.amazonaws.com/{}.mp4'.format(bucket,file_nm)
    transcribe.start_transcription_job(
        TranscriptionJobName=file_nm,
        Media={'MediaFileUri': job_uri},
        MediaFormat='mp4',
        LanguageCode='ko-KR',
        Settings={
            'ShowSpeakerLabels' : True, # 화자분리 기능 True or False
            'MaxSpeakerLabels' : 2 # 화자수
        }
    )

    # Transcribe job 작업이 끝나면 결과값 불러옴
    while True:
        status = transcribe.get_transcription_job(TranscriptionJobName=file_nm)
        if status['TranscriptionJob']['TranscriptionJobStatus'] in ['COMPLETED', 'FAILED']:
            save_json_uri = status['TranscriptionJob']['Transcript']['TranscriptFileUri']
            break

    # Transcribe 결과가 저장된 웹주소
    save_json_uri = status['TranscriptionJob']['Transcript']['TranscriptFileUri']

     # 웹서버 결과 파이썬으로 불러오기
    load = request.urlopen(save_json_uri)
    confirm = load.status
    rst = load.read().decode('utf-8')

    # 문자열을 딕셔너리로 변환 후 결과 가져오기
    transcribe_text = literal_eval(rst)['results']['transcripts'][0]['transcript']

    # 확인
    print(transcribe_text)

    # Job 삭제하기
    del_transcribe =boto3.client('transcribe', config=my_config)
    res = del_transcribe.delete_transcription_job(
    TranscriptionJobName = file_nm
    )

    res['ResponseMetadata']['HTTPStatusCode'] == '200'
    return analysisVIDText(transcribe_text)


#임시로 직접 제목 입력
if __name__ == '__main__':
    transcribe("elder")
