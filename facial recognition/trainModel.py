import cv2
import numpy as np
import glob
import pickle 
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score, classification_report
from skimage.feature import local_binary_pattern
import pandas as pd

def lbp(image):
    radius = 1
    neighbors = 8
    # Compute LBP features for the image
    lbpImage = local_binary_pattern(image, radius, neighbors)
    return lbpImage

def hog(image):
    winSize = (100, 100)       # Size of the detection window
    blockSize = (8, 8)      # Size of the block used for normalization
    blockStride = (4, 4)      # Stride between blocks
    cellSize = (8, 8)         # Size of cells used for computing histograms
    nbins = 9                  # Number of bins in the histogram

    # Create an HOG descriptor
    hog = cv2.HOGDescriptor(winSize, blockSize, blockStride, cellSize, nbins)
    # Compute HOG features for the image
    hogFeatures = hog.compute(image)
    return hogFeatures


img = [cv2.imread(file) for file in glob.glob("C:/Users/nik.glavic/Desktop/slike/*.jpg")]
print(len(img))


#testPercent = int(input("vnesite % učenja:"))
testPercent = 20

images=[]
for i in img:
    if(i is not None):
        i=cv2.resize(i,(100,100))
        images.append(i)
    else:
        print(i)

catsHOG=[]
catsLBP=[]
for i in images:
    gray_image= cv2.cvtColor(i, cv2.COLOR_BGR2GRAY)
    # Compute and add the HOG features to the list
    catsHOG.append(hog(gray_image))
    # Compute and add the LBP features to the list
    catsLBP.append(lbp(gray_image))

feature_array = []
for hog_feature, lbp_feature in zip(catsHOG, catsLBP):
    hog_flat = hog_feature.flatten()
    lbp_flat = lbp_feature.flatten()
    combined_feature = np.hstack((hog_flat, lbp_flat))
    feature_array.append(combined_feature)
    
members = []
for i in range(int(len(images))):
    if i < 300:
        members.append("mar")
    elif 301 < i < 600:
        members.append("nej")
    else:
        members.append("nik")
labels = np.array(members)

max_length = max(len(feature_array), len(labels))
if len(feature_array) < max_length:
    feature_array += [np.zeros_like(feature_array[0])] * (max_length - len(feature_array))
elif len(labels) < max_length:
    labels = np.hstack((labels, [None] * (max_length - len(labels))))