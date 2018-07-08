package com.example.memes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 최보선 on 2018-07-08.
 */

public class StatusBar extends AppCompatActivity{
    public void showExpandedlayoutNotification(){
        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,new Intent(this,MainTab.class),PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.normal_and_turtle);
        mBuilder.setTicker("상태변경");
        mBuilder.setContentTitle("거북목측정");
        mBuilder.setContentText("Hi,welecome to MEME");
        mBuilder.setDefaults(Notification.FLAG_NO_CLEAR);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.addAction("START",);
        mBuilder.addAction("STOP",stopService());
        mBuilder.setContentIntent(pendingIntent);


        nm.notify(111,mBuilder.build());


    }



}
