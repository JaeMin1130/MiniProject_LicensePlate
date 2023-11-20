import os
import time
import tensorflow as tf
import tensorflow_hub as hub
import matplotlib.pyplot as plt
import numpy as np
from PIL import Image


# ESRGAN 모델 불러오기
model = hub.load("https://www.kaggle.com/models/kaggle/esrgan-tf2/frameworks/TensorFlow2/variations/esrgan-tf2/versions/1")

# 이미지 전처리
# Image preprocessing
def preprocess_image(image_path):
    with open(image_path, 'rb') as file:
        image_data = file.read()
    
    hr_image = tf.image.decode_image(image_data)
    
    # Convert color image to black and white
    hr_image = tf.image.rgb_to_grayscale(hr_image)
    
    # Convert black and white image to color
    hr_image = tf.image.grayscale_to_rgb(hr_image)
    
    # Increased compatibility with other models (multiple of 4)
    hr_size = (tf.convert_to_tensor(hr_image.shape[:-1]) // 4) * 4
    hr_image = tf.image.crop_to_bounding_box(hr_image, 0, 0, hr_size[0], hr_size[1])
    hr_image = tf.cast(hr_image, tf.float32)
    return tf.expand_dims(hr_image, 0)


# 이미지 저장
def save_image(image, filename):
    if not isinstance(image, Image.Image):
        image = tf.clip_by_value(image, 0, 255)
        image = Image.fromarray(tf.cast(image, tf.uint8).numpy())
    image.save(filename)

# ESRGAN 함수
def model_result(input_path, output_path):
    hr_image = preprocess_image(input_path)
    start = time.time()
    fake_image = model(hr_image)
    fake_image = tf.squeeze(fake_image)
    print("Time Taken: %f" % (time.time() - start))
    save_image(tf.squeeze(fake_image), filename=output_path)
    print("Image processed and saved.")

if __name__ == "__main__":
    model_result()