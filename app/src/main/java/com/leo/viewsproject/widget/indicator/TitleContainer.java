package com.leo.viewsproject.widget.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.leo.viewsproject.R;
import com.leo.viewsproject.widget.indicator.indicator.IIndicatorView;
import com.leo.viewsproject.widget.indicator.title.IMeasureTitleView;
import com.leo.viewsproject.widget.indicator.title.ITitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe :
 * Created by Leo on 2019/4/15 on 15:52.
 */
public class TitleContainer extends FrameLayout implements IMove {

    private Context mContext;
    private BaseTitleAdapter mAdapter;
    private LinearLayout mTitleContainer;
    private IIndicatorView mIndicatiorView;
    private int mLastSelectedPos = -1;

    private List<PositionData> mPosDataList = new ArrayList<>();

    public TitleContainer(Context context) {
        this(context, null, 0);
    }

    public TitleContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setTitleView(BaseTitleAdapter adapter) {
        if (adapter == null) {
            return;
        }

        mAdapter = adapter;

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_indicator, this);
        mTitleContainer = rootView.findViewById(R.id.title_container);
        LinearLayout mIndicatorContainer = rootView.findViewById(R.id.indicator_container);
        for (int i = 0; i < mAdapter.getTotalCount(); i++) {
            ITitleView titleView = mAdapter.getTitleView(mContext, i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            layoutParams.setMargins(40, 40, 40, 40);
            ((View) titleView).setLayoutParams(layoutParams);
            mTitleContainer.addView(((View) titleView));

            if (i == 0) {
                titleView.onSelected(0, 0, mAdapter.getTotalCount());
                mLastSelectedPos = 0;
            }
        }

        if (mAdapter != null) {
            mIndicatiorView = mAdapter.getIndicatorView(mContext);
            if (mIndicatiorView instanceof View) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mIndicatorContainer.addView((View) mIndicatiorView, params);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter == null) {
            return;
        }

        initPositionDataList();
        if (mIndicatiorView != null) {
            mIndicatiorView.onPositionDataProvide(mPosDataList);
            mIndicatiorView.onSelected(0, -1, 0);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean isLeftToRight) { }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean isLeftToRight) { }

    @Override
    public void onSelected(int index, int lastIndex, int totalCount) {
        if (index < 0 || mTitleContainer == null || index > mTitleContainer.getChildCount() - 1) {
            return;
        }

        onDisSelected(mLastSelectedPos, totalCount);
        mIndicatiorView.onSelected(index, mLastSelectedPos, mAdapter.getTotalCount());

        View childAt = mTitleContainer.getChildAt(index);
        if (childAt != null) {
            ((ITitleView) childAt).onSelected(index, mLastSelectedPos, totalCount);
            mLastSelectedPos = index;
        }
    }

    @Override
    public void onDisSelected(int index, int totalCount) {
        if (index < 0 || mTitleContainer == null || index > mTitleContainer.getChildCount() - 1) {
            return;
        }

        View childAt = mTitleContainer.getChildAt(index);
        if (childAt != null) {
            ((ITitleView) childAt).onDisSelected(index, totalCount);
        }
    }

    private void initPositionDataList() {
        mPosDataList.clear();
        for (int i = 0; i < mAdapter.getTotalCount(); i++) {
            PositionData positionData = new PositionData();
            View childView = mTitleContainer.getChildAt(i);
            positionData.setLeft(childView.getLeft());
            positionData.setRight(childView.getRight());
            positionData.setTop(childView.getTop());
            positionData.setBottom(childView.getBottom());
            if (childView instanceof IMeasureTitleView) {
                IMeasureTitleView measureTitleView = (IMeasureTitleView) childView;
                positionData.setContentLeft(measureTitleView.getContentLeft());
                positionData.setContentRight(measureTitleView.getContentRight());
                positionData.setContentTop(measureTitleView.getContentTop());
                positionData.setContentBottom(measureTitleView.getContentBottom());
            } else {
                positionData.setContentLeft(positionData.getLeft());
                positionData.setContentRight(positionData.getRight());
                positionData.setContentTop(positionData.getTop());
                positionData.setContentBottom(positionData.getBottom());
            }

            mPosDataList.add(positionData);
        }
    }

    public int getSelectedIndex() {
        return mLastSelectedPos;
    }
}
