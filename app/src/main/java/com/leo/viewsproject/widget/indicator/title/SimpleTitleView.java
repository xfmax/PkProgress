package com.leo.viewsproject.widget.indicator.title;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Describe :
 * Created by Leo on 2019/4/15 on 16:04.
 */
public class SimpleTitleView extends AppCompatTextView implements IMeasureTitleView {

    private int mSelectedColor;
    private int mNormalColor;

    public SimpleTitleView(Context context) {
        this(context, null, 0);
    }

    public SimpleTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setPadding(10, 10, 10, 10);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MIDDLE);
    }

    public void setSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
    }

    public void setNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
        setTextColor(ContextCompat.getColor(getContext(), mNormalColor));
    }

    @Override
    public int getContentLeft() {
        return 0;
    }

    @Override
    public int getContentRight() {
        return 0;
    }

    @Override
    public int getContentTop() {
        return 0;
    }

    @Override
    public int getContentBottom() {
        return 0;
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean isLeftToRight) {

    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean isLeftToRight) {

    }

    @Override
    public void onSelected(int index, int lastIndex, int totalCount) {
        setTextColor(ContextCompat.getColor(getContext(), mSelectedColor));
    }

    @Override
    public void onDisSelected(int index, int totalCount) {
        setTextColor(ContextCompat.getColor(getContext(), mNormalColor));
    }
}
