package com.example.locationscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

public class ForegroundService extends Service {
    Handler handler = new Handler();
    Runnable runnable;
    FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 1. Create Notification Channel (Required for Android 8+)
        createNotificationChannel();

        // 2. Start Foreground (Must happen within 5 seconds of service start)
        startForeground(1, getNotification());

        // 3. Start the 5-minute loop
        startLocationLoop();

        return START_STICKY; // Restarts service if app is killed
    }

    //rethink later
    private Notification getNotification() {
        return null;
    }

    private void startLocationLoop() {
        runnable = new Runnable() {
            @Override
            public void run() {
                fetchLocation();
                handler.postDelayed(this, 300000);
            }
        };
        handler.post(runnable);
    }

    @SuppressLint("MissingPermission")
    private void fetchLocation() {
        // Permission check required by Android
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    // 1. Show Toast (Works because Handler runs on Main Thread)
                    Toast.makeText(this, "Loc: " + location.getLatitude(), Toast.LENGTH_SHORT).show();

                    // 2. Save to DB (Run this on background thread)
                    new Thread(() -> {
                        AppDatabase db = AppDatabase.getDbInstance(getApplicationContext());
                        db.locationDao().insert(new LocationEntity(
                                location.getLatitude(),
                                location.getLongitude(),
                                new Date().toString()));
                    }).start();
                }
            });
        }
    }

    public void createNotificationChannel() {
        final String CHANNELID = "ForegroundServiceChannel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(CHANNELID, "Location Service Channel", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(nc);
            }
        }

        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, CHANNELID);
        } else {
            notificationBuilder = new Notification.Builder(this);
        }

        Notification notification = notificationBuilder
                .setContentTitle("Location Tracker Active")
                .setContentText("Fetching location every 5 minutes...")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation) // Mandatory: Use a system icon for now
                .build();

        startForeground(1, notification);
    }
}
