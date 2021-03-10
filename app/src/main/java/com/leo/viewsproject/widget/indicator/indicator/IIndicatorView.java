package com.leo.viewsproject.widget.indicator.indicator;

import com.leo.viewsproject.widget.indicator.IMove;
import com.leo.viewsproject.widget.indicator.PositionData;

import java.util.List;

/**
 * Describe :
 * Created by Leo on 2019/4/15 on 15:56.
 */
public interface IIndicatorView extends IMove {
    void onPositionDataProvide(List<PositionData> positionDataList);
}
