import cv2
import numpy as np
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
 
def extract_features(image):
    lbp = cv2.LBP(radius=1, neighbors=8)
    features = lbp.compute(image)
    features = np.ravel(features)  
    return features
 
features_list = []
labels_list = []

face_cascade = cv2.CascadeClassifier('path_to_cascade_classifier.xml')
image = cv2.imread('path_to_image.jpg')
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))
for (x, y, w, h) in faces:
    face_roi = gray[y:y+h, x:x+w]
    features = extract_features(face_roi)
    features_list.append(features)
    labels_list.append(label)  

features_matrix = np.array(features_list)
labels = np.array(labels_list)

 
X_train, X_test, y_train, y_test = train_test_split(features_matrix, labels, test_size=0.2, random_state=42)
 
svm = SVC()

 
svm.fit(X_train, y_train)
 
y_pred = svm.predict(X_test)
 
accuracy = accuracy_score(y_test, y_pred)
print("Natančnost SVM modela:", accuracy)
