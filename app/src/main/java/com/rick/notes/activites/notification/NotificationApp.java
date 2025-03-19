package com.rick.notes.activites.notification;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rick.notes.R;

import java.util.Calendar;

public class NotificationApp extends Application {
    public static final String CHANNEL_ID = "Notification";

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationChannel();

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,11);
//        calendar.set(Calendar.MINUTE,31);
//        calendar.set(Calendar.SECOND,00);
//
//        if (Calendar.getInstance().after(calendar)) {
//            calendar.add(Calendar.DAY_OF_MONTH,1);
//        }
//
//        Intent intent;
//        intent = new Intent(NotificationApp.this, MemoBroadcast.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
//        }
    }
        private void NotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
