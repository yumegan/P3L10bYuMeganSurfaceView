package com.example.p3l10byumegansurfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class DrawView extends SurfaceView implements Runnable{
    private boolean mRunning;
    private Thread mGameThread = null;
    private SurfaceHolder mSurfaceHolder;
    private Context mContext;
    int x = 0;
    Paint mPaint = new Paint();
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
    }

    @Override
    public void run() {
        Canvas canvas;
        long frameStartTime;
        long frameTime;
        final int FPS = 60;
        while (mRunning) {
            if (mSurfaceHolder == null){return;}
            // If valid drawing surface...
            if (mSurfaceHolder.getSurface().isValid()) {
                //record start time for current run
                frameStartTime = System.nanoTime();
                //access the canvas
                canvas = mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.save();
                    // clear the screen using white
                    canvas.drawARGB(255, 0, 0, 0);
                    try {
                        // Your drawing here
                        drawMe(canvas);
                    } finally {  //unlock and release canvas
                        canvas.restore();
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                // calculate the time required to draw the frame in ms
                frameTime = (System.nanoTime() - frameStartTime) / 1000000;

                if (frameTime < (1000/FPS)) // if faster than the FPS -> wait until FPS matched
                {
                    try {
                        Thread.sleep((int)(1000/FPS) - frameTime);
                    } catch (InterruptedException e) {}
                }
            }
        }
    }
    private void drawMe(Canvas canvas) {//replaces the onDraw() from previous drawing lab
        mPaint.setColor(getResources().getColor(R.color.pink_light));
        canvas.drawRect(x++, 0, getWidth() / 2, getHeight() / 2, mPaint);
        mPaint.setColor(getResources().getColor(R.color.orange_light));
        canvas.drawRect((50+x++), 50, getWidth() / 2, getHeight() / 2+50, mPaint);
        mPaint.setColor(getResources().getColor(R.color.yellow_light));
        canvas.drawRect((100+x++), 100, getWidth() / 2, getHeight() / 2+100, mPaint);
        mPaint.setColor(getResources().getColor(R.color.green_light));
        canvas.drawRect((150+x++), 150, getWidth() / 2, getHeight() / 2+150, mPaint);
        mPaint.setColor(getResources().getColor(R.color.blue_light));
        canvas.drawRect((200+x++), 200, getWidth() / 2, getHeight() / 2+200, mPaint);
        mPaint.setColor(getResources().getColor(R.color.purple_light));
        canvas.drawRect((250+x++), 250, getWidth() / 2, getHeight() / 2+250, mPaint);
    }
    /**
     * Called by MainActivity.onPause() to stop the thread.
     */
    public void pause() {
        mRunning = false;
        try {
            // Stop the thread == rejoin the main thread.
            mGameThread.join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Called by MainActivity.onResume() to start a thread.
     */
    public void resume() {
        mRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }
    @Override  //resets x to 0 on touch
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            x = 0;
        }
        return true;
    }
}
