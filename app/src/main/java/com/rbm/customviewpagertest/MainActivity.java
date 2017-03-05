package com.rbm.customviewpagertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rbm.customviewpagertest.components.MyCustomViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MyCustomViewPager myCustomViewPager = (MyCustomViewPager)findViewById(R.id.custom_vp);
        myCustomViewPager.setData();


    }
}
