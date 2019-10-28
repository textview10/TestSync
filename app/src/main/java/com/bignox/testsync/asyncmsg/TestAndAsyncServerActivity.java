package com.bignox.testsync.asyncmsg;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bignox.testsync.R;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import java.util.ArrayList;

/**
 * @author xu.wang
 * @date 2019/10/28 15:12
 * @desc
 */
public class TestAndAsyncServerActivity extends AppCompatActivity {

    private static final String TAG = "TestAndAsServerActivity";

    private static final int WEB_SOCKET_PORT = 11001;

    private ArrayList<WebSocket> mLists = new ArrayList<>();
    private TextView tvServerDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_async_server);
        initializeView();
    }

    private void initializeView() {
        setTitle("Async server");
        Button btnStartServer = findViewById(R.id.btn_start_server);
        tvServerDetail = findViewById(R.id.tv_server_detail);
        btnStartServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServer();
            }
        });
    }

    private void startServer() {
        Log.e(TAG, " initialzie web socket ... ");
        AsyncHttpServer httpServer = new AsyncHttpServer();
        httpServer.setErrorCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                Log.e("WebSocket", "An error occurred", ex);
                tvServerDetail.setText("An error occurred 1 = " + ex.toString());
            }
        });
        httpServer.listen(AsyncServer.getDefault(), WEB_SOCKET_PORT);

//        String url = "ws://10.8.7.179:11001";
        httpServer.websocket("/", new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                mLists.add(webSocket);
                Log.e(TAG, " connected ...");
                //Use this to clean up any references to your websocket
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        final Exception tempEx = ex;
                        try {
                            if (ex != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("WebSocket", "An error occurred", tempEx);
                                        tvServerDetail.setText("An error occurred 2 = " + tempEx.toString());
                                    }
                                });
                            }
                        } finally {
                            mLists.remove(webSocket);
                        }
                    }
                });
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        Log.e(TAG, " on string available s = " + s);
                        final String result = s;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvServerDetail.setText(" on string available s = " + result);
                                if (TextUtils.equals("a string",result)){
                                    webSocket.send("receive string /a string/ .... and return a string ...");
                                }
                            }
                        });
                    }
                });

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
                    Log.e(TAG, " print stack = " + ex.toString());
                    return;
                }
                webSocket.send("a string");
                webSocket.send("adjkashfsajl 2");
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
