package com.leo.viewsproject.widget.indicator;

/**
 * Describe :
 * Created by Leo on 2019/4/16 on 10:49.
 */
public class PositionData {

    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;

    private int mContentLeft;
    private int mContentRight;
    private int mContentTop;
    private int mContentBottom;

    public int width() {
        return mRight - mLeft;
    }

    public int height() {
        return mBottom - mTop;
    }

    public int contentWidth() {
        return mContentRight - mContentLeft;
    }

    public int contentHeight() {
        return mContentBottom - mContentTop;
    }

    public int horizantalCenter() {
        return mLeft + width() / 2;
    }

    public int verticalCenter() {
        return mTop + height() / 2;
    }

    public void setLeft(int mLeft) {
        this.mLeft = mLeft;
    }

    public void setRight(int mRight) {
        this.mRight = mRight;
    }

    public void setTop(int mTop) {
        this.mTop = mTop;
    }

    public void setBottom(int mBottom) {
        this.mBottom = mBottom;
    }

    public void setContentLeft(int mContentLeft) {
        this.mContentLeft = mContentLeft;
    }

    public void setContentRight(int mContentRight) {
        this.mContentRight = mContentRight;
    }

    public void setContentTop(int mContentTop) {
        this.mContentTop = mContentTop;
    }

    public void setContentBottom(int mContentBottom) {
        this.mContentBottom = mContentBottom;
    }

    public int getLeft() {
        return mLeft;
    }

    public int getRight() {
        return mRight;
    }

    public int getTop() {
        return mTop;
    }

    public int getBottom() {
        return mBottom;
    }

    public int getContentLeft() {
        return mContentLeft;
    }

    public int getContentRight() {
        return mContentRight;
    }

    public int getContentTop() {
        return mContentTop;
    }

    public int getContentBottom() {
        return mContentBottom;
    }
}
