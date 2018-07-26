package com.example.memes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AutoRun extends BroadcastReceiver { //부팅 시 자동실행
    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();
        if(action.equals("android.intent.action.BOOT_COMPLETED")){
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
