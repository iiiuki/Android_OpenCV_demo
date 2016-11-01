package com.example.hiep.android_opencv.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.util.Log;

import com.example.hiep.android_opencv.R;
import com.example.hiep.android_opencv.system.MyApp;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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
        String path = initializeOpenCVDependencies();
        if (path==null){
            Log.e("OpenCVUtil", "Path is null");
            return null;
        }
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

    private static String initializeOpenCVDependencies() {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = MyApp.getInstance().getApplicationContext().getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir =  MyApp.getInstance().getApplicationContext().getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            // Load the cascade classifier
            return mCascadeFile.getAbsolutePath();

        } catch (Exception e) {
            Log.e("OpenCVUtil", "Error loading cascade", e);
            return null;
        }
    }
}
