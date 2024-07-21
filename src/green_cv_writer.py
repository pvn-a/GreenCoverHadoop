import numpy as np
import cv2
import os
import glob
import csv
import re

images = [cv2.imread(file) for file in sorted(glob.glob("dataset/*.png"))] #Reads all the image pixel arrays into images
image_names = [os.path.basename(x) for x in sorted(glob.glob('dataset/*'))] # Gets the image names in alphabetical order

txt_file = open('greens.txt', 'w', newline='')
txt_writer = csv.writer(txt_file, delimiter=' ')
# csv_writer.writerow(['Image_Name','Location','Green_Percentage'])

LOWER_G = np.array([0, 30, 0], np.uint8) # Lower bound for Green color in [B,G,R] index
UPPER_G = np.array([173, 255, 148], np.uint8) # Higher bound for Green color in [B,G,R] index

#LOWER_G = np.array([0, 25, 0], np.uint8)
#UPPER_G = np.array([173, 255, 154], np.uint8)

print("\n")
for index, image in enumerate(images):
    mask = cv2.inRange(image, LOWER_G, UPPER_G)
    # output = cv2.bitwise_and(image, image, mask=mask)
    Total_Number_Of_Pixels = mask.size # total_no_of_pixels = width * height of image
    no_of_green_pix = cv2.countNonZero(mask) #all non-zero pixels are green pixels
    green_percentage = no_of_green_pix/Total_Number_Of_Pixels * 100
    image_location = re.sub(r'\.png$', '', image_names[index])
    if(green_percentage > 75.0):
    	print(image_names[index], image_location.title(), green_percentage,sep='\t')
    txt_writer.writerow(
        [image_names[index], image_location.title(), green_percentage])

print("\n")
txt_file.close()
