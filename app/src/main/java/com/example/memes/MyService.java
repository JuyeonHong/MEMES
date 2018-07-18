package com.example.memes;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.content.Intent;
import android.app.Service;
import android.content.*;


public class MyService extends Service {

    /*private IBinder mIBinder = new MyBinder();


    class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }*/

    public MyService() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("LOG", "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("LOG", "서비스 시작");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("LOG", "서비스 종료");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LOG", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }


}