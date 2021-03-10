package com.leo.viewsproject.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
  * Describe : 可监听键盘弹出的输入框
  * Created by Leo on 2019/3/1.
  */
public class SoftKeyboardListenEditText extends AppCompatEditText implements ViewTreeObserver.OnGlobalLayoutListener {

    private OnSoftKeyboardDownListener listener;
    private boolean showSoft;
    private float screenHeight;

    public SoftKeyboardListenEditText(Context context) {
        this(context, null);
    }

    public SoftKeyboardListenEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftKeyboardListenEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        requestFocus();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("Leo onLayoutChange", "onLayoutChange");
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (listener != null) {
                listener.hideSoft();
            }
            super.onKeyPreIme(keyCode, event);
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setListener(OnSoftKeyboardDownListener listener) {
        this.listener = listener;
    }

    @Override
    public void onGlobalLayout() {

        Rect rect = new Rect();
        getGlobalVisibleRect(rect);

        if (listener == null) {
            return;
        }

        if (showSoft && rect.top > screenHeight * 2 / 3f) {
            showSoft = false;
            listener.hideSoft();
        } else if (!showSoft && rect.top < screenHeight * 2 / 3f) {
            showSoft = true;
            listener.showSoft();
        }
    }

    public interface OnSoftKeyboardDownListener {
        void hideSoft();

        void showSoft();
    }

    @Override
    protected void onDetachedFromWindow() {
        destroy();
        super.onDetachedFromWindow();
    }

    public void destroy() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
