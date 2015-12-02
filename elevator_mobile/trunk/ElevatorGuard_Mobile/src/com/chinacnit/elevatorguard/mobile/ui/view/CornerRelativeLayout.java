package com.chinacnit.elevatorguard.mobile.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.chinacnit.elevatorguard.mobile.R;

public class CornerRelativeLayout extends RelativeLayout {

    private int width;
    private int height;
    private Path pathToClip;
    private float cornerRadius;

    public CornerRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CornerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CornerRelativeLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        // filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG |
        // Paint.FILTER_BITMAP_FLAG);
        cornerRadius = getResources().getDimension(R.dimen.five_dp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initSizes() {
        pathToClip = new Path();
        pathToClip.addRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius,
                Direction.CW);
    }

    @Override
    public void draw(Canvas canvas) {
        int flag = canvas.save();
        canvas.clipPath(pathToClip);
        super.draw(canvas);
        canvas.restoreToCount(flag);
    };

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int flag = canvas.save();
        canvas.clipPath(pathToClip);
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(flag);
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        initSizes();
    }

    //	@Override
    //	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //		// int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    //		// int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    //		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    //		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    //		width = widthSize;
    //		height = heightSize;
    //		initSizes();
    //		setMeasuredDimension(width, height);
    //	}

}
