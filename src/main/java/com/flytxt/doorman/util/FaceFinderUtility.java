package com.flytxt.doorman.util;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

public class FaceFinderUtility {

	public static void detectFaces(String fileName, String destinationFolder, CascadeClassifier... faceDetectors) {
		new File(destinationFolder).mkdirs();
		Mat image = Imgcodecs.imread(fileName);

		int counter = 1;
		MatOfRect faceDetections = new MatOfRect();

		try {
			for (CascadeClassifier faceDetector : faceDetectors) {
				faceDetector.detectMultiScale(image, faceDetections);
				for (Rect rect : faceDetections.toArray()) {
					rect = extendRectangle(rect, 0.2f); // extending rectangle by 20%
					Mat faceROI = new Mat(image, rect);
					Imgcodecs.imwrite(
							destinationFolder + fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length() - 1)
									+ counter++ + ".jpg",
							faceROI);
				}
			}
		} catch (Exception e) {
			System.out.println("Problem with file: "+ fileName);
		}
	}

	private static Rect extendRectangle(Rect rect, float percent) {
		Size size = rect.size();

		Point topLeft = rect.tl();
		topLeft.x = topLeft.x - (size.width * (percent / 2));
		topLeft.y = topLeft.y - (size.height * (percent / 2));

		Point bottomRight = rect.br();
		bottomRight.x = bottomRight.x + (size.width * (percent / 2));
		bottomRight.y = bottomRight.y + (size.height * (percent / 2));

		return new Rect(topLeft, bottomRight);
	}
}
