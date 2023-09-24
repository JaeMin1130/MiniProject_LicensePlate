from roboflow import Roboflow

rf = Roboflow(api_key="IhcDYxqLRcjLRLZ2Dc1J")
project = rf.workspace().project("license_plate_recognition-k9hpj")
model = project.version(6).model

# 이미지 파일 경로 설정
image_path = None
def model_result(image_path):
    classes = []
    confidences = []
    # 이미지 데이터를 사용하여 예측 결과 가져오기
    result = model.predict(image_path, confidence=30, overlap=30).json()
    predictions = result['predictions']

    sorted_predictions = sorted(predictions, key=lambda x: x['x'])

    # 이전 예측의 x 값과 현재 예측의 x 값을 비교하여 차이가 5 미만인 경우 x 값을 통일
    prev_x = None
    for prediction in sorted_predictions:
        if prev_x is not None and abs(prediction['x'] - prev_x) < 5:
            prediction['x'] = prev_x
        prev_x = prediction['x']

    highest_confidence_by_x = {}

    for prediction in sorted_predictions:
        x = prediction['x']
        if x not in highest_confidence_by_x or prediction['confidence'] > highest_confidence_by_x[x]['confidence']:
            highest_confidence_by_x[x] = prediction

    for x, prediction in highest_confidence_by_x.items():
        classes.append(prediction["class"])
        confidences.append(prediction["confidence"])

    if not confidences:
        return ["식별 불가"]
    
    # Calculate the average confidence
    average_confidence = sum(confidences) / len(confidences) * 100

    # Concatenate the classes list to get the final result
    result_string = ''.join(classes)

    if len(result_string) >= 6:
        result_string = result_string[-4:]

    return [result_string, average_confidence]

if __name__ == "__main__":
    model_result()