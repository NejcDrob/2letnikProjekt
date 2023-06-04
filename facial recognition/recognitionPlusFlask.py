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

    
    # Return the prediction as JSON
    return jsonify(prediction=prediction)
    
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')