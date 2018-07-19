package com.example.memes.alarmscheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.memes.alarmscheduler.receiver.AlarmOneMinuteBroadcastReceiver;


public class AlarmUtils {
    //private final static int FIVE_SECOND = 5 * 1000;
    private final static int ONE_MINUTES = 60 * 1000;

    private static AlarmUtils _instance;

    public static AlarmUtils getInstance() {
        if (_instance == null) _instance = new AlarmUtils();
        return _instance;
    }

    public void startOneMinuteAlarm(Context context) {

        // AlarmOneMinuteBroadcastReceiver 초기화
        Intent alarmIntent = new Intent(context, AlarmOneMinuteBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        // 1분뒤에 AlarmOneMinuteBroadcastReceiver 호출 한다.
        startAlarm(context, pendingIntent, ONE_MINUTES);
    }


    private void startAlarm(Context context, PendingIntent pendingIntent, int delay) {

        // AlarmManager 호출
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 1분뒤에 AlarmOneMinuteBroadcastReceiver 호출 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }
    }
}
