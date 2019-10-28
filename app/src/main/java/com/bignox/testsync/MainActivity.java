package com.bignox.testsync;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeData();
    }

    private void initializeData() {
        Button btnTest = findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private void test() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                testCalculate(1);
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                testCalculate(2);
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                super.run();
                testCalculate(3);
            }
        }.start();
    }

    private synchronized void testCalculate(int index){
        SystemClock.sleep(5000);
        Log.e(TAG," test calculate index = " + index);
    }
}
