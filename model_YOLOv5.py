import os
import io
import shutil
from PIL import Image
import uuid
import torch
        
def YOLOv5(img_file_name, model, img_savefolder):
    img = Image.open(img_file_name)
    results = model([img])  # 모델에 이미지 전달하여 결과 받기
    results.render()
    target_class_index = 1
    confidence_threshold = 0.3
    for detection in results.pred[0]:
        x_min, y_min, x_max, y_max, confidence, class_num = detection
        if (
            detection is not None
            and confidence >= confidence_threshold 
            and class_num == target_class_index
        ):
            x_min = int(x_min)
            y_min = int(y_min)
            x_max = int(x_max)
            y_max = int(y_max)
            img_with_box = img.crop((x_min, y_min, x_max, y_max))
            #now_time = datetime.datetime.now().strftime(DATETIME_FORMAT)
            if not os.path.exists(img_savefolder):
                os.makedirs(img_savefolder)
                
            img_savename = f"{img_savefolder}/{uuid.uuid4()}.jpg"
            img_with_box.save(img_savename)
            
            return img_savename
    return None

def YOLOv5_Load(img_file_name, img_savefolder):
    model = torch.hub.load('ultralytics/yolov5','custom',path='./weights/yolov5_license_plate.pt')  # force_reload = recache latest code
    model.eval()
    result_img = YOLOv5(img_file_name, model, img_savefolder)
    return result_img
