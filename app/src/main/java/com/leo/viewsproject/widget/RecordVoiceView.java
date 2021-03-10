package com.leo.viewsproject.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import com.leo.viewsproject.R;

/**
 * Describe :
 * Created by Leo on 2019/5/6 on 10:22.
 */
public class RecordVoiceView extends View {

    private Context mContext;
    private Paint mPaint;
    private Paint mLeftPaint, mRightPaint, mDotPaint;
    private Paint mTextPaint;

    private int mRecordWidth, mRecordSize, mCenterY, mCenterX;
    private int mScreenWidth;

    private Rect textRect = new Rect();

    private Drawable recordDrawable;
    private IRecordState mRecordState;
    private int mCurrentRecordState = IRecordState.STATE_NORMAL;

    public RecordVoiceView(Context context) {
        this(context, null, -1);
    }

    public RecordVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RecordVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mPaint.setAntiAlias(true);

        mDotPaint = new Paint();
        mDotPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mDotPaint.setAntiAlias(true);

        mLeftPaint = new Paint();
        mLeftPaint.setColor(Color.RED);
        mLeftPaint.setAntiAlias(true);

        mRightPaint = new Paint();
        mRightPaint.setColor(Color.BLACK);
        mRightPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);

        mScreenWidth = 1440;
        setRecordStateValue();
        recordDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_settings_voice_black_24dp);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRecordSize = 360;
        mRecordWidth = w;
        mCenterY = h / 2;
        mCenterX = mRecordWidth / 2;
        mCurXPosition = mCenterX;
    }

    private int mCurXPosition, mLaseXPosition;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getCircleX(), mRecordSize / 2f, mRecordSize / 2f, mPaint);

        if (getCircleX() == mScreenWidth - mRecordSize / 8) {
//            canvas.drawCircle(getCircleX(), mCenterY, mRecordSize / 4f, mRightPaint);
            drawText(canvas, "发送");
        } else if (mCurXPosition == mRecordSize / 8f) {
//            canvas.drawCircle(getCircleX(), mCenterY, mRecordSize / 4f, mLeftPaint);
            drawText(canvas, "取消");
        } else {
            canvas.save();
            canvas.translate(getCircleX() - mRecordSize / 4f, mRecordSize / 4f);
            recordDrawable.setBounds(0, 0, mRecordSize / 2, mRecordSize / 2);
            recordDrawable.draw(canvas);
            canvas.restore();
        }

        if (isDownFinish) {
            drawDot(canvas);
        }
    }

    int space = 60, dotNum = 16;

    private void drawDot(Canvas canvas) {
        canvas.save();
        canvas.translate(getCircleX(), mCenterY);
        float angel = (float) ((2 * Math.PI) / dotNum);

        for (int j = 0; j < 5; j++) {

            int radio = mRecordSize / 2 + space * (j + 1);
            for (int i = 0; i < dotNum; i++) {
                float x = (float) (radio * Math.cos(i * angel));
                float y = (float) (radio * Math.sin(i * angel));
                mDotPaint.setColor(Color.parseColor("#" + (5 -j) + "" + (5 -j) + "D81B60"));
                canvas.drawCircle(x, y, 10f, mDotPaint);
            }
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas, String textStr) {
        canvas.save();

        mTextPaint.setTextSize(mRecordSize / 4f);
        mTextPaint.getTextBounds(textStr, 0, textStr.length(), textRect);
        int width = textRect.width();
        int height = textRect.height();

        canvas.drawText(textStr, getCircleX() - width / 2f, mCenterY + height / 3f, mTextPaint);
        canvas.restore();
    }

    private float getCircleX() {
        if (!isDownFinish) {
            mCurXPosition = mCenterX;
            return mCurXPosition;
        }

        if (mCurXPosition < mRecordSize / 8f) {
            mCurrentRecordState = IRecordState.STATE_RELEASE;
            mCurXPosition = Math.max(mRecordSize / 8, mCurXPosition - mRecordSize / 8);
        }

        if (mCurXPosition > mScreenWidth - mRecordSize / 8f) {
            mCurrentRecordState = IRecordState.STATE_SEND;
            mCurXPosition = mScreenWidth - mRecordSize / 8;
        }

        return mCurXPosition;
    }

    private boolean isDownFinish, hasMoved;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hasMoved = false;
                animate().cancel();
                animate().scaleX(0.55f).scaleY(0.55f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isDownFinish = true;
                        mCurrentRecordState = IRecordState.STATE_PRESS;
                        setRecordStateValue();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        mCurXPosition = mCenterX;
                        mCurrentRecordState = IRecordState.STATE_START_RECORD;
                        setRecordStateValue();
                        invalidate();
                    }
                }).start();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDownFinish) {
                    mCurXPosition = (int) event.getX();
                    if (hasMoved) {
                        invalidate();
                    } else {
                        if (mCurXPosition < mCenterX - mRecordSize / 4 || mCurXPosition > mCenterX + mRecordSize / 4) {
                            hasMoved = true;
                            invalidate();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setRecordStateValue();
                mLaseXPosition = mCurXPosition;
                resetBall();
                break;

        }
        return super.onTouchEvent(event);
    }

    private void resetBall() {
        animate().scaleX(1f).scaleY(1f).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float s = (float) animation.getAnimatedValue();
                mCurXPosition = (int) ((mCenterX - mLaseXPosition) * s + mLaseXPosition);
                invalidate();
            }
        }).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isDownFinish = false;
                mCurXPosition = mCenterX;
                mLaseXPosition = mCurXPosition;
                invalidate();
                mCurrentRecordState = IRecordState.STATE_RELEASE;
                setRecordStateValue();
            }
        }).setInterpolator(new AccelerateInterpolator()).start();
    }

    public void setRecordState(IRecordState mRecordState) {
        this.mRecordState = mRecordState;
    }

    private void setRecordStateValue() {

        if (mRecordState != null) {

            switch (mCurrentRecordState) {
                case IRecordState.STATE_START_RECORD:
                    mRecordState.onStartRecord();
                    break;
                case IRecordState.STATE_PRESS:
                    mRecordState.onRecord();
                    break;
                case IRecordState.STATE_RELEASE:
                    mRecordState.onCancel();
                    break;
                case IRecordState.STATE_SEND:
                    mRecordState.onSend();
                    break;
                default:
                    mRecordState.onStart();
                    break;
            }
        }
    }

    public interface IRecordState {

        int STATE_NORMAL = 0X1110;
        int STATE_START_RECORD = 0X1111;
        int STATE_PRESS = 0X1112;
        int STATE_RELEASE = 0X1113;
        int STATE_SEND = 0X1114;

        void onStart();

        void onStartRecord();

        void onRecord();

        void onSend();

        void onCancel();
    }
}