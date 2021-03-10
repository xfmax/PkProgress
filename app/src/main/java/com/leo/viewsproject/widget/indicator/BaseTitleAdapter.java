package com.leo.viewsproject.widget.indicator;

import android.content.Context;
import com.leo.viewsproject.widget.indicator.indicator.IIndicatorView;
import com.leo.viewsproject.widget.indicator.title.ITitleView;

/**
 * Describe :
 * Created by Leo on 2019/4/15 on 15:54.
 */
public abstract class BaseTitleAdapter {
    abstract int getTotalCount();

    abstract ITitleView getTitleView(Context context, int position);

    abstract IIndicatorView getIndicatorView(Context context);
}
