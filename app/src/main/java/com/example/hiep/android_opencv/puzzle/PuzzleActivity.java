package com.example.hiep.android_opencv.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;

/**
 * Created by Hiep on 10/27/2016.
 */
public class PuzzleActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener {
    private CameraBridgeViewBase mOpenCvCameraView;
    private Puzzle15Processor mPuzzle15;
    private int mGameWidth,mGameHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase) new JavaCameraView(this, -1);
        // set view cho mainActivity là giao diện hiển thị từ camera
        setContentView(mOpenCvCameraView);
        mOpenCvCameraView.enableView();

        mOpenCvCameraView.setCvCameraViewListener(this);
        mPuzzle15 = new Puzzle15Processor();
        mPuzzle15.prepareNewGame();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGameWidth = width;
        mGameHeight = height;
        mPuzzle15.prepareGameSize(width, height);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        return mPuzzle15.puzzleFrame(inputFrame);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onTouch(mOpenCvCameraView,event);
        return super.onTouchEvent(event);
    }

    /* Khi chạm vào mành hình, chúng ta sẽ xác định vị trí của ô mà người chơi chạm vào, nếu gần với ô trống, sẽ đổi chỗ 2 ô này */

    public boolean onTouch(View view, MotionEvent event) {
        int xpos, ypos;

        xpos = (view.getWidth() - mGameWidth) / 2;
        xpos = (int)event.getX() - xpos;

        ypos = (view.getHeight() - mGameHeight) / 2;
        ypos = (int)event.getY() - ypos;

        if (xpos >=0 && xpos <= mGameWidth && ypos >=0  && ypos <= mGameHeight) {
            /* click is inside the picture. Deliver this event to processor */
            mPuzzle15.deliverTouchEvent(xpos, ypos);
        }

        return false;
    }
}
