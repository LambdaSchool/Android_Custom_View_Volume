package com.example.customspinner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import static java.lang.Math.abs;

public class VolumeSpinner extends android.support.v7.widget.AppCompatImageView {

    public static final int VOL_UPPER_BOUND = 175;
    public static final int VOL_LOWER_BOUND = -100;
    public static final int GET_AND_SET_VOLUME_MAX_VALUE = 10;
    public static final int LEFT_JUSTIFY = 140;
    public static final int TOP_JUSTIFY = 430;
    public static final int X_PIVOT_JUSTIFY = 2;
    public static final int Y_PIVOT_JUSTIFY = 43;
    float x = 0, y = 0;
    float touchX, touchY;
    float rotation = 0;
    private Paint paint;
    private Bitmap bitmap;

    public VolumeSpinner(Context context) {
        super(context);
        init(context);
    }

    public VolumeSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VolumeSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        Resources resources = context.getResources();
        bitmap = BitmapFactory
                .decodeResource(resources, R.drawable.knobtrans);

        bitmap = Bitmap.createScaledBitmap(bitmap, 700, 700, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float diffX = event.getX() - touchX;
                rotation += diffX / 3;
                touchX = event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rotation < VOL_LOWER_BOUND) rotation = VOL_LOWER_BOUND; //keep rotation in bounds
        if (rotation > VOL_UPPER_BOUND) rotation = VOL_UPPER_BOUND;
        canvas.rotate(rotation, getPivotX() + X_PIVOT_JUSTIFY, getPivotY() + Y_PIVOT_JUSTIFY); //finely tuned pivot points
        canvas.drawBitmap(bitmap, LEFT_JUSTIFY, TOP_JUSTIFY, paint);
        //canvas.drawCircle(getPivotX() + X_PIVOT_JUSTIFY, getPivotY() + Y_PIVOT_JUSTIFY, 15, paint); //reenable to see pivot point
    }

    public float getVolume() { //returns a float 0-10
        float range = abs(VOL_LOWER_BOUND) + abs(VOL_UPPER_BOUND);
        float volume = abs(VOL_LOWER_BOUND)+rotation;
        volume = (volume/range)* GET_AND_SET_VOLUME_MAX_VALUE;
        return volume;
    }
    public void setVolume(float volume) { //interprets anything above GET_AND_SET_VOLUME_MAX_VALUE as the max value
        float range = abs(VOL_LOWER_BOUND) + abs(VOL_UPPER_BOUND);
        volume = (volume/ GET_AND_SET_VOLUME_MAX_VALUE) * range;
        rotation = volume;
    }
}
