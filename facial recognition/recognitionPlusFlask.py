import cv2   
import numpy 
import pandas 
import glob   
import pickle 
import base64
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score 
from sklearn.metrics import classification_report
from skimage.feature import local_binary_pattern
from flask import Flask, request, jsonify # uvozi knižnico flask za flask


app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict_from_image():
    def lbp(image):
        radius = 1
        neighbors = 8
        lbp_image = local_binary_pattern(image, radius, neighbors)
        return lbp_image

    def hog(image):
        resized_image = cv2.resize(image, (100, 100))
        winSize = (100, 100)       # Size of the detection window
        blockSize = (8, 8)      # Size of the block used for normalization
        blockStride = (4, 4)      # Stride between blocks
        cellSize = (8, 8)         # Size of cells used for computing histograms
        nbins = 9                  # Number of bins in the histogram

        hog = cv2.HOGDescriptor(winSize, blockSize, blockStride, cellSize, nbins)
        hog_features = hog.compute(resized_image)
        return hog_features

    with open('model_123.pkl', 'rb') as file:
        model = pickle.load(file)

    def decode_image(base64_string):
        decoded_data = base64.b64decode(base64_string)
        np_data = numpy.frombuffer(decoded_data, numpy.uint8)
        image = cv2.imdecode(np_data, cv2.IMREAD_COLOR)
        return image
    
    if 'image' not in request.json:
        print('No image part')
        return jsonify(error='No image part in the request'), 400

    image_base64 = request.json['image']
    
    test_image = decode_image(image_base64)

    processed_test_images = []
    if image is not None:
        image = cv2.resize(image, (100, 100))
        gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        processed_test_images.append(gray_image)

    HOG_test_features = []
    LBP_test_features = []
    for image in processed_test_images:
        HOG_features = hog(image)
        LBP_features = lbp(image)
        HOG_test_features.append(HOG_features)
        LBP_test_features.append(LBP_features)

    combined_test_features = []
    for HOG_feat, LBP_feat in zip(HOG_test_features, LBP_test_features):
        HOG_flat = HOG_feat.flatten()
        LBP_flat = LBP_feat.flatten()
        combined_features = numpy.hstack((HOG_flat, LBP_flat))
        combined_test_features.append(combined_features)

    test_dataframe = pandas.DataFrame(combined_test_features)
    test_dataframe.fillna(test_dataframe.mean(), inplace=True)
    cleaned_test_features = test_dataframe.values.tolist()

    if not cleaned_test_features:
        print("No test features found.")
        exit()

    predictions = model.predict(cleaned_test_features)

    for image, prediction in zip(test_image, predictions):
        print("Prediction:", prediction)
        cv2.waitKey(0)

    return jsonify(prediction=prediction)
    
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')