package mdy.com.mview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2017/9/15.
 */

public class PView extends View {

    private int mWidth;
    private int mHeight;

    private Paint mPaint;

    private Paint cPaint;

    private Paint textPaint;

    private float percent;

    private float yUp;

    private ValueAnimator valueAnimator;
    private ValueAnimator valueAnimator1;

    public PView(Context context) {
        super(context);
        init();
    }

    public PView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        cPaint=new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setColor(Color.WHITE);

        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(10);
        textPaint.setTextSize(100);

        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.color_29A3FE));

        startAnimator();
    }

    private void startAnimator(){

        //波形移动
        valueAnimator= ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(800);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                percent= (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.start();

        //进度上升
        valueAnimator1=ValueAnimator.ofFloat(0,1);
        valueAnimator1.setDuration(10000);
        valueAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator1.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator1.setInterpolator(new LinearInterpolator());
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                yUp= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator1.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save(Canvas.CLIP_SAVE_FLAG);

        Path path=new Path();

        path.addCircle(mWidth/2,mHeight/2,mWidth/2, Path.Direction.CW);

        canvas.drawPath(path,cPaint);
        canvas.clipPath(path);

        initWaveText(canvas,(int)(yUp*100)+"%");
        
        initWaveView(canvas);
    }

    private void initWaveText(Canvas canvas,String text) {

        textPaint.setColor(getResources().getColor(R.color.color_29A3FE));

        drawCenterText(canvas,textPaint,text);

    }

    private void drawCenterText(Canvas canvas, Paint textPaint, String text) {
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;

        int centerY = (int) (rect.centerY() - top / 2 - bottom / 2);

        canvas.drawText(text, rect.centerX(), centerY, textPaint);
    }

    private void initWaveView(Canvas canvas) {

        Path path=new Path();

        int x=-mWidth;

        int y=mHeight;

        x+=percent*mWidth;
        y-=mHeight*yUp;

        path.moveTo(x,y);

        int  quadWidth=mWidth/4;
        int quadHeight=mHeight/20*3;


        path.rQuadTo(quadWidth,-quadHeight,quadWidth*2,0);
        path.rQuadTo(quadWidth,quadHeight,quadWidth*2,0);

        path.rQuadTo(quadWidth,-quadHeight,quadWidth*2,0);
        path.rQuadTo(quadWidth,quadHeight,quadWidth*2,0);

        path.lineTo(mWidth,mHeight);
        path.lineTo(-mWidth,mHeight);

        path.close();

        canvas.drawPath(path,mPaint);
        canvas.save();
        canvas.clipPath(path);
        textPaint.setColor(Color.WHITE);
        drawCenterText(canvas,textPaint,(int)(yUp*100)+"%");
        canvas.restore();

    }
}
