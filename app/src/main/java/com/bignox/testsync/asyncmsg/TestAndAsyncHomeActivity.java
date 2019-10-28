package com.bignox.testsync.asyncmsg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bignox.testsync.R;

/**
 * @author xu.wang
 * @date 2019/10/28 15:23
 * @desc
 */
public class TestAndAsyncHomeActivity extends AppCompatActivity {

    private Button btnServer;
    private Button btnClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_async_home);

        initializeView();
    }

    private void initializeView() {
        btnServer = findViewById(R.id.btn_home_server);
        btnClient = findViewById(R.id.btn_home_client);

        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestAndAsyncHomeActivity.this, TestAndAsyncServerActivity.class);
                startActivity(intent);
            }
        });
        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestAndAsyncHomeActivity.this, TestAndAsyncClientActivity.class);
                startActivity(intent);
            }
        });
    }
}
