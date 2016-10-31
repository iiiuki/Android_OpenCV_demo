package com.example.hiep.android_opencv.utils;

import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.util.Log;

import com.example.hiep.android_opencv.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by Hiep on 10/27/2016.
 */
public class OpenCVUtil {

    public static Bitmap effect(Bitmap bmInput){
        Bitmap output = Bitmap.createBitmap(bmInput.getWidth(),
                bmInput.getHeight(),
                Bitmap.Config.ARGB_8888);
        Mat source = new Mat();
        Utils.bitmapToMat(bmInput, source);
        Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(source, source, new Size(3, 3), 0);
        Imgproc.adaptiveThreshold(source, source, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);
        Utils.matToBitmap(source, output);
        return output;
    }

    public static Bitmap detectFace(Bitmap bmInput){
        Bitmap output = Bitmap.createBitmap(bmInput.getWidth(),
                bmInput.getHeight(),
                Bitmap.Config.ARGB_8888);
        String path = "android.resource://" + "com.example.hiep.android_opencv" + "/" + R.raw.lbpcascade_fontalface;
        CascadeClassifier faceDetector = new CascadeClassifier(path);
        Mat source = new Mat();
        Utils.bitmapToMat(bmInput, source);
        // Detect faces in the image.
        MatOfRect faceDetections =new MatOfRect();
        faceDetector.detectMultiScale(source, faceDetections);
        Log.d("Hiep",String.format("--------- Detected %s faces", faceDetections.toArray().length));

        for (Rect rect :faceDetections.toArray()){
            Imgproc.rectangle(source, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }
        Utils.matToBitmap(source, output);
        return output;
    }
}
