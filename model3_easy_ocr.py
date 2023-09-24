import easyocr
import cv2

# 언어 선택 / GPU 사용여부 (현재 : CPU)
reader = easyocr.Reader(['en'], gpu=False)

# 이미지 삽입 경로
input_path = None

def model_result(image_path):
    result = reader.readtext(image_path, allowlist='0123456789')

    max_confidence = -1  # 초기화
    selected_text = None

    for detection in result:
        bbox = detection[0]
        text = detection[1]
        confidence = detection[2]

        # 가장 높은 신뢰도를 가진 텍스트 선택
        if confidence > max_confidence:
            max_confidence = confidence
            selected_text = text
            selected_bbox = bbox

    if selected_text:
        print(f'Class: {selected_text}, Confidence: {max_confidence:.2f}')
        return [selected_text[-4:], max_confidence*100]
    else:
        return (print("None"))

if __name__ == "__main__":
    model_result()