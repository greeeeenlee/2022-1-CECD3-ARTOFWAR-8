# -*- coding: utf-8 -*-
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers,optimizers
from tensorflow.keras.applications.vgg16 import VGG16
from keras.preprocessing.image import ImageDataGenerator
import numpy as np
import os
import numpy as np
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True 
import matplotlib.pyplot as plt

#VGG16 모델 구성
conv_base = VGG16(weights='imagenet', 
      include_top=False, 
      input_shape=(150, 150, 3))

#네트워크 구성
model = keras.Sequential()
model.add(keras.Input(shape=(150,150,3)))
model.add(conv_base) #VGG16 합성곱 기반층
model.add(layers.Flatten())
model.add(layers.Dense(256, activation='relu'))
model.add(layers.Dense(1, activation='sigmoid'))

#훈련, 검증, 테스트 데이터셋 경로 설정
base_dir = '/content/drive/My Drive/data/'
train_dir = os.path.join(base_dir, 'train')
validation_dir = os.path.join(base_dir, 'val')
test_dir = os.path.join(base_dir, 'test')

#훈련, 검증 데이터 전처리
train_datagen = ImageDataGenerator( #훈련 데이터 증식
      rescale=1./255, #이미지를 1/255로 스케일을 조정
      rotation_range=20,
      width_shift_range=0.1,
      height_shift_range=0.1,
      shear_range=0.1,
      zoom_range=0.1,
      horizontal_flip=True,
      fill_mode='nearest')
test_datagen = ImageDataGenerator(rescale=1./255) #검증 데이터 증식 X

train_generator = train_datagen.flow_from_directory(
        train_dir,
        target_size=(150, 150), #이미지 크기를 150 × 150로 변경
        batch_size=20,
        class_mode='binary') #이진 레이블 사용
validation_generator = test_datagen.flow_from_directory(
        validation_dir,
        target_size=(150, 150),
        batch_size=20,
        class_mode='binary')

#합성곱 동결
conv_base.trainable=True
set_trainable=False
#최상위 합성곱 5 블록 3개 층 동결 제외 - 미세 조정
for layer in conv_base.layers:
  if layer.name == 'block5_conv1':
    set_trainable=True
  if set_trainable:
    layer.trainable=True
  else:
    layer.trainable=False

#모델 학습 방식 설정 
model.compile(loss='binary_crossentropy', #유해-중립 이진 클래스 분류 
      optimizer=optimizers.RMSprop(lr=1e-5),  #학습률을 낮춘 RMSProp 옵티마이저 - 미세 조정
      metrics=['acc'])

#모델 학습 
history = model.fit(
      train_generator,
      steps_per_epoch=100,
      epochs=100,
      validation_data=validation_generator,
      validation_steps=50,
      verbose=2)

#테스트 데이터 전처리
test_generator = test_datagen.flow_from_directory(
        test_dir,
        target_size=(150, 150),
        batch_size=8,
        class_mode='binary')
#모델 평가 
loss_and_metrics = model.evaluate(test_generator)