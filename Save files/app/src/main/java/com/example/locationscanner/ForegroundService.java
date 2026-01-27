package com.example.locationscanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class ForegroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            Log.e("service","Sevice running");
                            try{
                                //5 mins
                                Thread.sleep(300000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                    }
                }
        ).start();
        final String CHANNELID="Foreground Service id";
        NotificationChannel nc=new NotificationChannel(CHANNELID,CHANNELID,NotificationManager.IMPORTANCE_LOW);

        getSystemService(NotificationManager.class).createNotificationChannel(nc);
        Notification.Builder notification=new Notification.Builder(this,CHANNELID).setContentText("Service is running")
                .setContentTitle("Location getter");

        startForeground(12,notification.build());
        return super.onStartCommand(intent, flags, startId);
    }
}
