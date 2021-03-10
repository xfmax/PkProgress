package com.leo.viewsproject.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.leo.viewsproject.R;
import com.leo.viewsproject.widget.PKProgressBar;
import com.leo.viewsproject.widget.indicator.CustomTitleAdapter;
import com.leo.viewsproject.widget.indicator.TitleContainer;

public class IndicatorActivity extends AppCompatActivity {

    private TitleContainer titleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        titleContainer = findViewById(R.id.indicatorView);
        CustomTitleAdapter customTitleAdapter = new CustomTitleAdapter();
        titleContainer.setTitleView(customTitleAdapter);
        customTitleAdapter.setTabClick(new CustomTitleAdapter.TabClick() {
            @Override
            public void onSelected(int position, int totalCount) {
                titleContainer.onSelected(position, -1, totalCount);
            }
        });
    }
}
