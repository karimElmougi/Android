package com.kevsoft.level;

/**
 * Created by kevku on 2016-10-19.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;


/**
 * Created by kevku on 2016-10-01.
 */

public class LevelView extends View implements Runnable {

    private static final int ANIMATION_FACTOR = 20;
    private float posYRollCircle;
    private float posXPitchCircle;

    private boolean running = false;
    private Thread process = null;
    private float posX;
    private float posY;
    private int circleRadius = 100;
    private int textSize = 64;

    private float pitch;
    private float roll;
    private float azimuth;

    public LevelView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!running){
            posX =  getWidth() / 2;
            posY = getHeight() / 2;
            posYRollCircle = getHeight() - getHeight()/8;
            posXPitchCircle = getWidth()/2;
            start();
        }

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.RED);
        canvas.drawCircle(posX, posYRollCircle, circleRadius, paint);
        canvas.drawCircle(posXPitchCircle, posY, circleRadius, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        canvas.drawText(String.format("%.1f", -roll)+"°", posX-textSize, (float)(posYRollCircle+textSize/2.0), paint);
        canvas.drawText(String.format("%.1f", -pitch)+"°", posXPitchCircle-textSize, (float)(posY+textSize/2.0), paint);
    }

    @Override
    public void run(){
        while(running){
            posY = 3 * getHeight() * (1 + pitch/90) / 8;
            posX = getWidth() * (1 - roll/90) / 2;

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });

            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {
                System.out.println("Process stopped!");
            }
        }


    }

    public void start(){
        if(!running){
            process = new Thread(this);
            process.start();
            running = true;
        }
    }

    public void setPitch(float p){
        pitch = p;
    }

    public void setRoll(float r){
        roll = r;
    }

    public void setAzimuth(float a){
        azimuth = a;
    }
}

