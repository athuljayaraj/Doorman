package com.flytxt.doorman;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.objdetect.CascadeClassifier;

import com.flytxt.doorman.util.FaceFinderUtility;

public class Doorman {

	private CascadeClassifier frontalFaceDetector = new CascadeClassifier(
			Doorman.class.getClassLoader().getResource("lbpcascade_frontalface.xml").getPath());
	private CascadeClassifier profileFaceDetector = new CascadeClassifier(
			Doorman.class.getClassLoader().getResource("lbpcascade_profileface.xml").getPath());

	private void findFaces(File file) {
		FaceFinderUtility.detectFaces(file.toString(), file.getParent() + "/output/", frontalFaceDetector,
				profileFaceDetector);
	}

	private void runAndFindFaces(File directory) {
		File[] directoryListing = directory.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isDirectory() && !child.getName().equals("output")) {
					runAndFindFaces(child);
				} else {
					findFaces(child);
				}
			}
		}

	}

	public static void main(String[] args) {

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Doorman obj = new Doorman();
		File dir = new File(args[0]);
		obj.runAndFindFaces(dir);

	}
}