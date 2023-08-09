package com.thinkdiffai.futurelove.CustomView;

import android.animation.AnimatorInflater;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.thinkdiffai.futurelove.R;

public class Custom_loading extends View {
    private int bgColor = Color.parseColor("#DAF5F4");
    private int textColor = Color.parseColor("#4873A6"); // default color
    // tells the compiler that the value of a variable
    // must never be cached as its value may change outside
    private volatile double progress = 0.0;
    private ValueAnimator valueAnimator;

    // It updates the percentages
    private final ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            progress = ((Float) valueAnimator.getAnimatedValue()).doubleValue();
            invalidate();    // redraw the screen
            requestLayout(); // when rectangular progress dimension changes
        }
    };

    // call after downloading is completed
    public void hasCompletedDownload() {
        // cancel the animation when the file is downloaded
        valueAnimator.cancel();
        invalidate();
        requestLayout();
    }

    // initialize
    public Custom_loading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Custom_loading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(
                context,
                // properties for downloading progress are defined
                R.animator.loading_animation
        );
        valueAnimator.addUpdateListener(updateListener);

        // initialize custom attributes of the button
        TypedArray attr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressLineView,
                0,
                0
        );
        try {

            // button background color
            bgColor = attr.getColor(
                    R.styleable.ProgressLineView_bgColor,
                    ContextCompat.getColor(context, R.color.dividerColor)
            );

            // button text color
            textColor = attr.getColor(
                    R.styleable.ProgressLineView_textColor,
                    ContextCompat.getColor(context, R.color.black)
            );
        } finally {
            // clearing all the data associated with attribute
            attr.recycle();
        }
    }

    // set attributes of paint
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // start the animation when the button is clicked
    public void animation() {
        valueAnimator.start();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Set the dimensions and radius of the rounded rectangle
        float x = 0f;
        float y = 0f;
        float radius = 80f;

        paint.setStrokeWidth(0f);
        paint.setTextSize(Float.parseFloat("40"));
        paint.setColor(bgColor);
        // draw custom button

        canvas.drawRoundRect(new RectF(x, y, x + getWidth(), y + getHeight()), radius, radius, paint);
        // to show rectangular progress on custom button while the file is downloading
        paint.setColor(Color.parseColor("#AE72D2"));
        canvas.drawRoundRect(new RectF(x, y, (getWidth() * (float) (progress / 100)), y + getHeight()), radius, radius, paint);
        // check the button state
        String buttonText = String.valueOf((int) progress/10) + "/10"; // Loading percentage values
        // write the text on the custom button
        paint.setColor(Color.parseColor("#4873A6"));
        // Tạo đối tượng Shader gradient
        canvas.drawText(buttonText, getWidth() / 2, (getHeight() + 30) / 2, paint);
    }
}
