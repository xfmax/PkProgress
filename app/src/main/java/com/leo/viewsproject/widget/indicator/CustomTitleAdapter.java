package com.leo.viewsproject.widget.indicator;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;
import com.leo.viewsproject.R;
import com.leo.viewsproject.widget.indicator.indicator.IIndicatorView;
import com.leo.viewsproject.widget.indicator.indicator.ImageIndicatorView;
import com.leo.viewsproject.widget.indicator.title.ITitleView;
import com.leo.viewsproject.widget.indicator.title.SimpleTitleView;

/**
 * Describe :
 * Created by Leo on 2019/4/15 on 15:57.
 */
public class CustomTitleAdapter extends BaseTitleAdapter {

    private TabClick mTabClick;
    private SparseArray<String> titles = new SparseArray<>();

    public CustomTitleAdapter() {
        titles.put(0, "公聊");
        titles.put(1, "私聊");
        titles.put(2, "小喇叭");
        titles.put(3, "大喇叭");
        titles.put(4, "语音");
    }

    @Override
    int getTotalCount() {
        return titles.size();
    }

    @Override
    ITitleView getTitleView(final Context context, final int position) {
        SimpleTitleView view = new SimpleTitleView(context);
        view.setText(titles.get(position));
        view.setNormalColor(R.color.colorPrimary);
        view.setSelectedColor(R.color.colorAccent);
        view.setBackgroundResource(R.drawable.shape_frame);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTabClick != null) {
                    mTabClick.onSelected(position, titles.size());
                }
            }
        });
        return view;
    }

    @Override
    IIndicatorView getIndicatorView(Context context) {
        ImageIndicatorView indicatorView = new ImageIndicatorView(context);
        indicatorView.setImageResource(R.mipmap.ic_launcher);
        return indicatorView;
    }

    public void setTabClick(TabClick mTabClick) {
        this.mTabClick = mTabClick;
    }

    public interface TabClick {
        void onSelected(int position, int totalCount);
    }
}
