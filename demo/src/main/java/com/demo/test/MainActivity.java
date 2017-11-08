package com.demo.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.toolbox.OkHttpStack;

import core.base.rxvolley.NetRequest;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetRequest.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER, new OkHttpStack(new OkHttpClient())));
    }
}
