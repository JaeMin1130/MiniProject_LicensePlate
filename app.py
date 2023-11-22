import base64
import uuid
from flask import Flask, render_template, request, redirect, jsonify
from flask_cors import CORS, cross_origin
import boto3
from botocore.exceptions import NoCredentialsError
from dotenv import load_dotenv
import argparse
import io
import os
os.environ['KMP_DUPLICATE_LIB_OK']='True'
from PIL import Image
import datetime
import torch
from tempfile import NamedTemporaryFile
import model1_YOLOv5 as yolo
import model2_esrgan_super_resolution as esrgan
import model3_easy_ocr as ocr
import model4_roboflow_license_number_extractor as robo
import model5_num_classification_YOLO as ncy

load_dotenv() 

app = Flask(__name__)

DATETIME_FORMAT = "%Y-%m-%d_%H:%M:%S"
# 번호판 짤린 이미지 저장할 폴더 경로
img_savefolder ="./test"

# 이미지 이름
file_name = str(uuid.uuid4()) + ".jpg"
# 이미지 흑백 변경후 이미지
after_path = f"./super_resolution/{file_name}"

@cross_origin()
@app.route("/api/records", methods=["POST"])
def predict():
    if request.method == "POST":
        if "file" not in request.files:
                return jsonify({"status":500, "error": "No file part"})

        file = request.files["file"]
        if not file:
            return jsonify({"status":500, "error": "No selected file"})

        img_bytes = file.read()

        # 1. YOLOv5s : 현장 사진에서 번호판 인식
        try:
            with NamedTemporaryFile(delete=False, suffix=".jpg") as temp_img_file:
                temp_img_file.write(img_bytes)
                temp_img_file_name = temp_img_file.name
                file_name = yolo.YOLOv5_Load(temp_img_file_name, img_savefolder)
        except Exception as e:
            return jsonify({"status":500, "error": str(e)}) 
        
        if file_name == None:
            return jsonify({"status":500, "error": "번호판 인식 불가"}) 

        file_path_name = f"./{file_name}"
        print("step1 passed")

        # 2. ESRGAN : 번호판 선명하게
        try:
            esrgan.model_result(file_path_name, after_path)
            print("file_path_name", file_path_name)
            print("step2 passed")
        except Exception as e:
            os.remove(file_path_name)
            return jsonify({"status":500, "error": str(e)}) 

        # 3. OCR
        ocr_result = ocr.model_result(after_path)
        print("step3 passed")

        # 4. CNN
        robo_result = robo.model_result(after_path)
        print("step4 passed")

        # 5. YOLOv5x
        try:
            yolo_result = ncy.load_yolo(after_path)
            print("step5 passed")
        except Exception as e:
            return jsonify({"status":500, "error": str(e)}) 


        # ESRGAN 거친 이미지 base64로 변환(json serializable)
        img_data = io.BytesIO()
        plate_image = Image.open(after_path)
        plate_image.save(img_data, format='JPEG')
        img_data.seek(0)
        base64_img = base64.b64encode(img_data.read()).decode('utf-8')

        os.remove(file_path_name)
        os.remove(after_path)

        return jsonify({
                "status" : 200,
                "predictedResults":[yolo_result, ocr_result, robo_result],
                "plateImg": base64_img
            })    
    
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Flask app exposing yolov5 models")
    parser.add_argument("--port", default=5000, type=int, help="port number")
    args = parser.parse_args()
    app.run(host="1.252.90.210") 