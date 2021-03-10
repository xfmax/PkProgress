package com.leo.viewsproject.widget.indicator;

/**
 * Describe :
 * Created by Leo on 2019/4/15 on 17:01.
 */
public interface IMove {

    void onEnter(int index, int totalCount, float enterPercent, boolean isLeftToRight);

    void onLeave(int index, int totalCount, float leavePercent, boolean isLeftToRight);

    void onSelected(int index, int lastIndex, int totalCount);

    void onDisSelected(int index, int totalCount);
}
