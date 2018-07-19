package com.example.memes.alarmscheduler.receiver;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.memes.MainTab;
import com.example.memes.alarmscheduler.AlarmUtils;

public class AlarmOneMinuteBroadcastReceiver extends AlarmBroadCastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context,intent);

        // 알람 스타트
        AlarmUtils.getInstance().startOneMinuteAlarm(context);
        Toast.makeText(context,"Passed one minute.",Toast.LENGTH_SHORT).show();
    }
}
