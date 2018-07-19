package com.example.memes.alarmscheduler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.memes.alarmscheduler.Constants;

public class AlarmBroadCastReceiver extends BroadcastReceiver {
    public static boolean isLaunched = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        isLaunched = true;

        // 현재 시간을 화면에 보낸다.
        saveTime(context);
    }

    private void saveTime(Context context) {
        long currentTime = System.currentTimeMillis();
        Intent intent = new Intent(Constants.INTENTFILTER_BROADCAST_TIMER);
        intent.putExtra(Constants.KEY_DEFAULT, currentTime);
        context.sendBroadcast(intent);
    }
}
