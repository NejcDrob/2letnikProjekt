import cv2
import numpy as np
import glob
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from joblib import dump, load
from sklearn.svm import SVC
from sklearn.ensemble import RandomForestClassifier
import os
import tensorflow as tf


def razdeli_podatke(images, labels, testSize):
    XTrain, XTest, yTrain, yTest = train_test_split(images, labels, test_size=testSize, random_state=80, stratify=labels)
    return XTrain, XTest, yTrain, yTest


def hog(image, cellSize, blockSize, blockStride, binNumber):
    winSize = (image.shape[1] // cellSize[0] * cellSize[0], image.shape[0] // cellSize[1] * cellSize[1])
    hogDescriptor = cv2.HOGDescriptor(winSize, blockSize, blockStride, cellSize, binNumber)
    hogFeature = hogDescriptor.compute(image)
    hogFeature = hogFeature.flatten()
    return hogFeature



def lbp(image):
    height, width = image.shape
    lbpImage = np.zeros((height - 2, width - 2), dtype=np.uint8)

    for i in range(1, height - 1):
        for j in range(1, width - 1):
            center = image[i, j]
            binaryPattern = 0

            for di in [-1, 0, 1]:
                for dj in [-1, 0, 1]:
                    if di == 0 and dj == 0:
                        continue

                    neighbor = image[i + di, j + dj]
                    binaryPattern <<= 1
                    if neighbor >= center:
                        binaryPattern |= 1

            lbpValue = binaryPattern
            lbpImage[i - 1, j - 1] = lbpValue

    return lbpImage

# Load the trained model
model = tf.keras.models.load_model("trained_model.h5")

def predict_animal(image_path):
    img = cv2.imread(image_path)
    img = cv2.resize(img, (100, 100))
    grayImage = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    lbpImage = lbp(grayImage)
    hogDescriptor = hog(grayImage, (8, 8), (16, 16), (8, 8), 10)
    featureVector = np.concatenate((lbpImage.flatten(), hogDescriptor))
    featureVector = np.array([featureVector])  # Reshape for model input
    predictions = model.predict(featureVector)
    animal_classes = ['nik', 'mar', 'nej']
    predicted_class_index = np.argmax(predictions[0])
    predicted_class = animal_classes[predicted_class_index]
    return predicted_class

# Example usage
image_path = "C:/Users/nik.glavic/Desktop/slike/nik(1).jpg"
predicted_animal = predict_animal(image_path)
print("Predicted animal:", predicted_animal)