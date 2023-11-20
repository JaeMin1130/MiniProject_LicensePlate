from PIL import Image
import torch
from operator import itemgetter
from keras.models import load_model
import tensorflow as tf


# 클래스 이름 설정 (클래스 이름 리스트가 있다면 해당 리스트 사용)
class_names = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']  # 예시 클래스 이름

def YOLOv5(file_path, model):
    img = Image.open(file_path)
    results = model([img])  # 모델에 이미지 전달하여 결과 받기
    results.render()
    confidence_threshold = 0.5
    objects = []
    test = 1
    return_classes = []
    return_confidence =[]

    for class_num, class_name in results.names.items():  # 클래스별로 반복
        for detection in results.pred[0]:
            x_min, y_min, x_max, y_max, confidence, detected_class = detection
            if (
                detection is not None
                and confidence >= confidence_threshold 
                and detected_class == class_num
            ):
                x_min = int(x_min)
                y_min = int(y_min)
                x_max = int(x_max)
                y_max = int(y_max)
                img_with_box = img.crop((x_min, y_min, x_max, y_max))
                
                objects.append((x_min, class_name, confidence, img_with_box))
    
    # x 좌표를 기준으로 객체들을 정렬
    objects.sort(key=itemgetter(0))
    for obj in objects:
        x_min, class_name, confidence, img_with_box = obj
        
        # return값 반환
        return_classes.append(class_name)
        return_confidence.append(confidence)
        # 리스트 요소를 문자열로 변환하고 연결한 후 정수로 변환
        num = int(''.join(map(str, return_classes[-4:])))
        avg = sum(return_confidence[-4:]) / 4
        
    # 가상의 TensorFlow Tensor 생성
    tensor = tf.constant(avg)

    # TensorFlow Tensor을 Python 데이터 타입으로 변환
    python_value = float(tensor.numpy())*100

    # 반환값 저장
    return {"modelType" : "yolo", "predictedText" : str(num), "accuracy" : python_value}

def load_yolo(after_path):
    model = torch.hub.load('ultralytics/yolov5','custom',path='./weights/yolov5_num_classifiaction.pt')  # force_reload = recache latest code
    result = YOLOv5(after_path, model)
    return result