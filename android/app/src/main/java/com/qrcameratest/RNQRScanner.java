package com.qrcameratest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.UnsupportedEncodingException;

public class RNQRScanner extends ReactContextBaseJavaModule {

    RNQRScanner(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        this.broadcast = new ScannerReceiver();
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(ACTION_SCANNER_RESULT);
        reactContext.registerReceiver(this.broadcast, this.intentFilter);
    }


    @Override
    public String getName() {
        return "RNQRScanner";
    }

    private static ReactApplicationContext reactContext;
    private BroadcastReceiver br;
    private ScannerReceiver broadcast;
    private IntentFilter intentFilter;


    private static final String ACTION_SCANNER_RESULT =
            "zoomsmart.intent.SCANNER_STOP_DONE";
    private static final String ACTION_SCANNER_STOP =
            "zoomsmart.intent.SCANNER_STOP";


    @ReactMethod
    public void createSubscription() {
        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                byte[] data = intent.getByteArrayExtra("SCAN_BARCODE1");
                String QRmsg = "";
                try {
                    QRmsg = new String(data, 0, data.length, "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                WritableNativeMap params = new WritableNativeMap();
                params.putString("msg", QRmsg);
                sendEvent("QRscannerMessage", params);
            }
        };
        // регистрируем (включаем) BroadcastReceiver
        reactContext.registerReceiver(br, new IntentFilter("zoomsmart.intent.SCANNER_OUTPUT"));
    }

    @ReactMethod
    void removeSubscription() {
        reactContext.unregisterReceiver(br);
    }


    @ReactMethod
    public void turnScannerOff(Callback onSuccess) {
        Log.d("RNQRScanner", "Scanner turning off");
        stopScanner(1);
        onSuccess.invoke();
    }

    @ReactMethod
    public void turnScannerOn(Callback onSuccess) {
        Log.d("RNQRScanner", "Scanner turning on");
        stopScanner(0);
        onSuccess.invoke();
    }


    void sendEvent(String event, WritableNativeMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(event, params);
    }

    public static void stopScanner(int i) {
        Log.d("[RNQRScanner]", "stopScanner " + i);

        Intent intent = new Intent(ACTION_SCANNER_STOP);
        intent.setAction(ACTION_SCANNER_STOP);
        intent.putExtra("FLAG", i);
        reactContext.sendBroadcast(intent);
    }

    class ScannerReceiver extends BroadcastReceiver {
        ScannerReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.d("[RNQRScanner]", "ScannerReceiver onReceive ");

            /* if (CloseScannerActivity.ACTION_SCANNER_RESULT.equals(intent.getAction())) {
                CloseScannerActivity.this.gotoCamera();
            } */
        }
    }
}
