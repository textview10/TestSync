package com.bignox.testsync;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author xu.wang
 * @date 2019/10/25 12:05
 * @desc
 */
public class BlueToothActivity extends AppCompatActivity {

    private static final String TAG = "BlueToothActivity";

    private BluetoothManager bluetoothmanger;
    private BluetoothAdapter bluetoothadapter;

    private BlueToothReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initializeView();
    }

    private void initializeView() {
        bluetoothmanger = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothadapter = bluetoothmanger.getAdapter();
        if (bluetoothadapter == null) {
            Toast.makeText(BlueToothActivity.this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        }

        mReceiver = new BlueToothReceiver();
        Button btnBlueTooth = findViewById(R.id.btn_bluetooth);
        Button btnStartDiscovery = findViewById(R.id.btn_start_discovery);
        Button btnGetInfomation = findViewById(R.id.btn_get_infomation);
        Button btnSetName = findViewById(R.id.btn_set_name);

        btnStartDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDiscovery();
            }
        });

        btnBlueTooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBlueToothEnable()) {
                    Log.e(TAG, " is blue tooth enable ......");
                    return;
                }
                openBlueTooth();
            }
        });

        btnGetInfomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfomation();
            }
        });
        btnSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBluetoothName();
            }
        });
    }

    private void setBluetoothName() {
        bluetoothadapter.setName("111111111 xiaomi ");
    }

    private void getInfomation() {
        String name = bluetoothadapter.getName();
        String address = bluetoothadapter.getAddress();
        Log.e(TAG," get infomation name = " + name
                + " address = " + address);
    }


    private void startDiscovery() {
        Log.e(TAG, "start discovery ... ");
        if (bluetoothadapter.isDiscovering()) {
            bluetoothadapter.cancelDiscovery();
        }
        bluetoothadapter.startDiscovery();
    }

    /**
     * 打开蓝牙...
     */
    private void openBlueTooth() {
        Log.e(TAG, " open blue tooth ...");
//        Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        startActivityForResult(enable, 1);
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);//默认是120秒

        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);//设置持续时间（最多300秒）

        startActivity(discoveryIntent);
    }


    /**
     * 判断蓝牙是否开启
     *
     * @return
     */
    public boolean isBlueToothEnable() {
        if (bluetoothadapter.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    class BlueToothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothDevice.ACTION_FOUND:
                    Log.e(TAG, "device found ...");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int bondState = device.getBondState();
                    String address = device.getAddress();
                    String name = device.getName();
                    Log.e(TAG, " address = " + address + " name = " + name
                            + " bondState = " + bondState);
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Log.e(TAG, " bluetooth adapter action state changed ...");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Log.e(TAG, " action discovery started... ");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.e(TAG, "action discovery finished... ");
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    Log.e(TAG, " action bond state changed");
                    break;
                default:
                    Log.e(TAG, "other broadcast = " + intent.getAction());
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);//找到设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        return filter;
    }
}
