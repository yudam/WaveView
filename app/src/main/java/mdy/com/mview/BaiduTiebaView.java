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
 * 绘制波浪线区域--裁剪圆形--绘制底层文字--绘制波浪线区域文字--举起你们的双手动起来
 */

public class BaiduTiebaView extends View{

    private int mWidth;
    private int mHeight;

    private float percent;

    private Paint cPaint;
    private Paint mPaint;
    private Paint textPaint;

    private String text="国";

    private ValueAnimator valueAnimator;

    public BaiduTiebaView(Context context) {
        super(context);

        init();
    }

    public BaiduTiebaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        cPaint=new Paint();
        cPaint.setColor(Color.WHITE);
        cPaint.setAntiAlias(true);

        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.color_29A3FE));

        textPaint=new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(15);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(120);

        startAnimator();
    }

    private void startAnimator(){

        valueAnimator=ValueAnimator.ofFloat(0,1);
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

        //保留画布裁剪区域
        canvas.save(Canvas.CLIP_SAVE_FLAG);

        //裁剪成圆形
        Path path=new Path();

        path.addCircle(mWidth/2,mHeight/2,mWidth/2, Path.Direction.CW);

        canvas.drawPath(path,cPaint);

        //裁剪画布
        canvas.clipPath(path);

        initWaveText(canvas);
        initWavePath(canvas);

    }

    private void initWaveText(Canvas canvas) {

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

    private void initWavePath(Canvas canvas) {

        Path path=new Path();

        int x=-mWidth;

        x+=(percent*mWidth);

        int quadWidth=mWidth/4;

        int quadHeight=mHeight/18*3;

        path.moveTo(x,mHeight/2);
        //第一个周期
        path.rQuadTo(quadWidth,-quadHeight,quadWidth*2,0);
        path.rQuadTo(quadWidth,quadHeight,quadWidth*2,0);
        //第二个周期
        path.rQuadTo(quadWidth,-quadHeight,quadWidth*2,0);
        path.rQuadTo(quadWidth,quadHeight,quadWidth*2,0);

        //绘制最右边竖直线
        path.lineTo(mWidth,mHeight);

        //绘制最低边直线
        path.lineTo(-mWidth,mHeight);

        //闭合区间
        path.close();
        //绘制波浪线
        canvas.drawPath(path,mPaint);
        //保存当前状态绘制剪切文字
        canvas.save();
        canvas.clipPath(path);
        textPaint.setColor(Color.WHITE);

        drawCenterText(canvas,textPaint,text);
        //回滚
        canvas.restore();

    }
}
