import tensorflow
from tensorflow.keras.models import load_model
import numpy as np
from tensorflow.keras.preprocessing import image
import cv2  
import boto3
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
model = load_model('../image_classification/image_classification.h5') #유해성 판별 모델 경로

#동영상의 유해성 판별 함수
def predictVid(video_title):
    save_file_path=download_video(video_title)
    vidcap = cv2.VideoCapture(save_file_path)  # 동영상이 있는 경로
    count = 0 #동영상 속 유해성 판별 횟수

    while(vidcap.isOpened()): #동영상 읽기
        ret, image = vidcap.read()
        try:
            image = cv2.resize(image, (150, 150))# 이미지 사이즈 150x150 변경
            
            if(int(vidcap.get(1)) % 30 == 0): # 30프레임당 하나씩 이미지 추출
                predict=predictImg(image) # 추출된 이미지 유해성 확률
                
                if round(predict[0][0],2) >0.5:# 추출된 이미지 유해성 판정
                    count += 1 
        except:
            vidcap.release()
            break

    result= 0 if count<3 else 1 # 0: 중립 동영상 1:유해 동영상
    delete_video(save_file_path) #동영상 삭제
    return result #유해성 결과 반환

#동영상의 이미지 유해성 판별 함수
def predictImg(img):
    # (150, 150, 3) 크기의 넘파이 float32 배열
    x = image.img_to_array(img)
    x/= 255.
        # 차원을 추가하여 (1, 150, 150, 3) 크기의 배치로 배열을 변환합니다
    x = np.expand_dims(x, axis=0)
    return model.predict(x)
  
# s3로부터 해당 동영상 파일 다운로드 함수
def download_video(video_title):
    s3=boto3.resource('s3')
    bucket = s3.Bucket('aowbuket')
    
    obj_file = video_title+'.mp4'
    save_file ='./'+video_title+'.mp4'
    bucket.download_file(obj_file, save_file)
    return save_file
  
#다운받은 동영상 삭제 함수
def delete_video(file_path):
    if os.path.exists(file_path):
        os.remove(file_path)
