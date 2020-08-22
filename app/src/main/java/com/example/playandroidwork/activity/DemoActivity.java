package com.example.playandroidwork.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.playandroidwork.R;
import com.example.playandroidwork.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity {

    private ArrayList<CustomTabEntity> mTabEntities     = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("测试1"));
        tabLayout.addTab(tabLayout.newTab().setText("测试2"));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            tabAt.setCustomView(R.layout.tab_item_layout);
            View customView = tabAt.getCustomView();
            TextView textView = customView.findViewById(R.id.tv_content);
            View view = customView.findViewById(R.id.img_view);

            if (i == 0) {
                textView.setText("测试1");
                view.setVisibility(View.VISIBLE);
            } else {
                textView.setText("测试2");
                view.setVisibility(View.INVISIBLE);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.img_view).setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.img_view).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mTabEntities.add(new TabEntity("渐变测试1"));
        mTabEntities.add(new TabEntity("渐变测试2"));

        CommonTabLayout commonTabLayout = findViewById(R.id.tl_9);
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setIndicatorHeight(10);

        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }


}