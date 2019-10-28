package com.bignox.testsync.asyncmsg;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bignox.testsync.R;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

/**
 * @author xu.wang
 * @date 2019/10/28 15:14
 * @desc
 */
public class TestAndAsyncClientActivity extends AppCompatActivity {

    private static final String TAG = "TestAndAsyCActivity";

    private Button btnConnectWebSocket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_andasync_client);
        initialzieView();
    }

    private void initialzieView() {
        setTitle("Async client");

        btnConnectWebSocket = findViewById(R.id.btn_connect_websocket);

        btnConnectWebSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectWebSocketServer();
            }

        });


    }

    private void connectWebSocketServer() {
        String url = "ws://10.8.7.179:11001";
        Log.e(TAG, " connect web socket server ...");

        AsyncHttpClient.getDefaultInstance().websocket(url, null, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    ex.printStackTrace();
                    Log.e(TAG," print stack = " + ex.toString());
                    return;
                }
                webSocket.send("a string");
                webSocket.send(new byte[10]);
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        Log.e(TAG, "I got a string: " + s);
                    }
                });
                webSocket.setDataCallback(new DataCallback() {
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                        Log.e(TAG, "I got some bytes!");
                        // note that this data has been read
                        byteBufferList.recycle();
                    }
                });
            }
        });
    }
}
