package com.base.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.base.core.log.L;

public class MainActivity extends AppCompatActivity {

    private String baseLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.e("TAG", "test");
    }
}
