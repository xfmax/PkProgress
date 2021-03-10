package com.leo.viewsproject.widget.indicator.indicator;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.leo.viewsproject.widget.indicator.PositionData;

import java.util.List;

/**
 * Describe :
 * Created by Leo on 2019/4/16 on 11:14.
 */
public class ImageIndicatorView extends AppCompatImageView implements IIndicatorView {

    private List<PositionData> mPositionDataList;

    public ImageIndicatorView(Context context) {
        this(context, null, 0);
    }

    public ImageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPositionDataProvide(List<PositionData> positionDataList) {
        mPositionDataList = positionDataList;
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean isLeftToRight) {}

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean isLeftToRight) {}

    @Override
    public void onSelected(int index, int lastIndex, int totalCount) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        PositionData currentPos = mPositionDataList.get(index);

        if (lastIndex == -1) {
            setTranslationX(currentPos.horizantalCenter() - getWidth() / 2f);
            return;
        }

        animate().translationX(currentPos.horizantalCenter() - getWidth() / 2f).setDuration(500).start();
    }

    @Override
    public void onDisSelected(int index, int totalCount) {}
}
