# GreenCoverHadoop

This project aims to detect places in Banglore city, which have a green cover(forest/parks etc) of more than X% (75% here). We are trying to provide an alternative approach to counting these places using Hadoop MapReduce.

## Overview:

Satellite images were manually selected, using a screen snip tool, from Google Maps. Important regions in Bangalore were selected. Each image is a picture of the entire area/ward/park (rather than having multiple zoomed-in images of the same place ) and no two images represent the same region. All the images are stored in the `dataset` folder.

`green_cv_writer.py`  identifies how much part of the image is in some shade of green using OpenCV. Challenges were faced in determining the appropriate lower and upper green [B, G, R] indexes, to avoid as much interference between plant cover and lakes, street shadows, etc. `Green% = (Total_Green_pixels*100)/Total_no_of_image_pixels`.

This program is then run on the entire dataset and the output is written to `greens.txt` with contents as follows: <filename> <location> <Green%>

`Greens_main.java` displays the number of locations with more than 75% green cover implementing the Hadoop MapReduce technique.
- The GreensMapper class reads in the input line by line and then tokenizes it with a default delimiter(space). The first two tokens are skipped and the third token(green%) is checked if it passes the threshold value. On favorable outcome, a key-value pair of <”Total_areas”,{count}> is passed to the reducer class, where {count} will be an IntWritable instance.
- The GreensReducer takes in the key-value input from the mapper. The IntWritable is iterated through to find the sum of total areas satisfying the threshold green% value. The output will be in the format:  <”Total_areas”,”sum”>.

An alternative `Greens_alt.java` program displays output in the format <{area_name},{green%}>.