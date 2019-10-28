package com.bignox.testsync.andserver;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bignox.testsync.R;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author xu.wang
 * @date 2019/10/25 15:23
 * @desc
 */
public class TestAndServerActivity extends AppCompatActivity {
    private static final String TAG = "TestAndServerActivity";

    private Server mServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_andserver);
        initializeView();
    }

    private void initializeView() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                initializeServer();
                startServer();
            }
        }.start();
    }


    /**
     * Create server.
     */
    public void initializeServer() {
        InetAddress inetAddress = getLocalIPAddress();
        if (inetAddress == null){
            return;
        }
        Log.e(TAG,"initialize server innet address" + inetAddress.getHostAddress());
        mServer = AndServer.serverBuilder(this)
                .inetAddress(inetAddress)
                .port(11111)
                .timeout(10, TimeUnit.SECONDS)
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                        Log.e(TAG, " the server start successfully ... ");
                    }

                    @Override
                    public void onStopped() {
                        Log.e(TAG, " the server has stopped ...");
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e(TAG, "an exception occurred while the server was starting");
                    }
                }).build();
    }

    /**
     * Start server.
     */
    public void startServer() {
        if (mServer.isRunning()) {
            Log.e(TAG, " the server start ...");
        } else {
            mServer.startup();
        }
    }

    /**
     * Stop server.
     */
    public void stopServer() {
        if (mServer.isRunning()) {
            mServer.shutdown();
        } else {
            Log.w("AndServer", "The server has not started yet.");
        }
    }

    public static InetAddress getLocalIPAddress() {
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                NetworkInterface nif = enumeration.nextElement();
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                if (inetAddresses != null) {
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && isIPv4Address(inetAddress.getHostAddress())) {
                            return inetAddress;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check if valid IPV4 address.
     *
     * @param input the address string to check for validity.
     *
     * @return True if the input parameter is a valid IPv4 address.
     */
    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }


    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                    "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
}
