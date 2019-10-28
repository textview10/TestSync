package com.bignox.testsync;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncServerSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xu.wang
 * @date 2019/10/24 20:43
 * @desc
 */
public class TestAndroidAsyncActivity extends AppCompatActivity {

    private static final String TAG = "TestAndroidAsynActivity";

    private AsyncHttpServer mAsyncHttpServer = new AsyncHttpServer();

    private AsyncServer mAsyncServer = AsyncServer.getDefault();

    private static final int ASYNC_PORT = 11000;

    private static final int WEB_SOCKET_PORT = 11001;

    private ArrayList<WebSocket> mLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_android_asyn);

        initializeView();

        initializeData();
    }

    private void initializeView() {
        Button btnLauncher = findViewById(R.id.btn_launcher);
        btnLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"launch btn ...");
            }
        });

        final Button btnWebSocket = findViewById(R.id.btn_web_socket);
        btnWebSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebSocketServer();
            }
        });

        Button btnOpenConnect = findViewById(R.id.btn_open_connect);
        btnOpenConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectWebSocket();
            }
        });
    }

    private void initializeData() {
//        AsyncServerSocket mServerSocket = mAsyncHttpServer.listen(mAsyncServer, ASYNC_PORT);



//        mAsyncHttpServer.post("/upload", new HttpServerRequestCallback() {
//            @Override
//            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
//                MultipartFormDataBody formDataBody = (MultipartFormDataBody) request.getBody();
//
//            }
//        });

    }


    private void initializeWebSocketServer() {

        Log.e(TAG, " initialzie web socket ... ");
        AsyncHttpServer httpServer = new AsyncHttpServer();
        httpServer.setErrorCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                Log.e("WebSocket", "An error occurred", ex);
            }
        });
        httpServer.listen(AsyncServer.getDefault(), WEB_SOCKET_PORT);


        httpServer.websocket("/live", new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                mLists.add(webSocket);

                //Use this to clean up any references to your websocket
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        try {
                            if (ex != null)
                                Log.e("WebSocket", "An error occurred", ex);
                        } finally {
                            mLists.remove(webSocket);
                        }
                    }
                });
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        Log.e(TAG, " on string available s = " + s);

                    }
                });

            }
        });
    }


    private void connectWebSocket() {
        String url = "";
        AsyncHttpClient.getDefaultInstance().websocket(url, "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    ex.printStackTrace();
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
