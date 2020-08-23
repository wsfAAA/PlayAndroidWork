package com.example.playandroidwork.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.playandroidwork.R;
import com.example.playandroidwork.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

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

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            data.add("测试: " + i);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        TestAdapter adapter = new TestAdapter(data);
        recyclerView.setAdapter(adapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Log.e("wsf", position + "  " + data.size());
                return position == data.size() - 1 ? 2 : 1;
            }
        });
    }


    public class TestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public TestAdapter(@Nullable List<String> data) {
            super(R.layout.test_adapter_item_layout, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, String s) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.getView(R.id.ll_view).getLayoutParams();
//            if (holder.getAdapterPosition() == getData().size()-1) {
//                layoutParams.width = ConvertUtils.dp2px(142);
//                layoutParams.height = ConvertUtils.dp2px(80);
//            } else {
//                layoutParams.width = ConvertUtils.dp2px(63);
//                layoutParams.height = ConvertUtils.dp2px(80);
//            }

            if (holder.getAdapterPosition() != 0 && holder.getAdapterPosition() % 3 == 0) {
                layoutParams.rightMargin = 0;
            }
        }
    }


}