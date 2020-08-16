package com.example.playandroidwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.example.base.net.RxClient;
import com.example.base.net.api.ApiService;
import com.example.base.net.callback.RxCallBack;
import com.example.base.net.http.OkhttpRequest;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.git).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxClient.builder().addHttpRequest(new OkhttpRequest()).build().rxGet(ApiService.BANNER, new RxCallBack<String>() {
                    @Override
                    public void rxOnNext(String response) {
                        Log.e("wsf", response);
                        ToastUtils.showShort(response);
                    }

                    @Override
                    public void rxOnError(Throwable e) {
                        Log.e("wsf", "rxOnError:  " + e.getMessage());
                    }
                });
            }
        });
    }
}