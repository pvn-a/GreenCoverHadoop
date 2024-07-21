import numpy as np
import cv2
import os
import glob
import csv
import re

LOWER_G = np.array([0, 30, 0], np.uint8) # Lower bound for Green color in [B,G,R] index
UPPER_G = np.array([173, 255, 148], np.uint8) # Higher bound for Green color in [B,G,R] index

txt_file = open('greens_temp.txt', 'w', newline='')
txt_writer = csv.writer(txt_file, delimiter=' ')

print("\n")
def printer(filename):
    image = cv2.imread(filename)
    size=image.shape;
    image1 = image
    mask = cv2.inRange(image, LOWER_G, UPPER_G)
    for i in range(size[0]):
	    for j in range(size[1]):
		    if(image1[i][j][1] > image1[i][j][2]):
			    pass
		    else:
			    if(mask[i][j] == 255):
				    mask[i][j] = 0
    dst = mask
    Total_Number_Of_Pixels=dst.size
    no_of_green_pix = cv2.countNonZero(dst)
    Index=no_of_green_pix/Total_Number_Of_Pixels * 100
    #print("Index: ",Index);print()
    return Index

count=0
for file in sorted(glob.glob("dataset/*.png")):
    filename = file.split('/')[1]
    index = printer(file)
    print(filename, filename.split('.')[0], index, sep=" ")
    txt_writer.writerow([filename, filename.split('.')[0], index])
    count+=1;print(count);print()

txt_file.close()