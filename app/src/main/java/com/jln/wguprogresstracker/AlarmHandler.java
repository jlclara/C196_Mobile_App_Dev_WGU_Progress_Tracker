package com.jln.wguprogresstracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import com.jln.wguprogresstracker.Activity.AddAssessment;
import com.jln.wguprogresstracker.Activity.MainActivity;

public class AlarmHandler extends BroadcastReceiver {

    public static final String alarmFile = "alarmFile";
    public static final String nextAlarmField = "nextAlarmId";


    @Override
    public void onReceive(Context context, Intent intent) {

        int nextAlarmId = intent.getIntExtra("nextAlarmId", getAndIncrementNextAlarmId(context));

        NotificationHelper notificationHelper = new NotificationHelper(context);


        if (intent.hasExtra("assessment_name")) {
            String assessmentName = intent.getStringExtra("assessment_name");
            String courseName = intent.getStringExtra("course_name");
            Integer alarmId = intent.getIntExtra("id", -1);

            NotificationCompat.Builder nb = notificationHelper.getChanne21Notifcation("Reminder: Complete an assessment today!", assessmentName + " for " + courseName);
            notificationHelper.getManager().notify(alarmId, nb.build());
        }
        else if (intent.hasExtra("id") && intent.hasExtra("course_name") && intent.hasExtra("start_alert_title")) {
            String courseName = intent.getStringExtra("course_name");
            String termName = intent.getStringExtra("term_name");
            String title = intent.getStringExtra("start_alert_title");
            Integer alarmId = intent.getIntExtra("id", -1);
            NotificationCompat.Builder nb = notificationHelper.getChannel1Notifcation(title, courseName + " in " + termName);
            notificationHelper.getManager().notify(alarmId, nb.build());
        }
        else if (intent.hasExtra("id") && intent.hasExtra("course_name") && intent.hasExtra("end_alert_title")) {
            String courseName = intent.getStringExtra("course_name");
            String termName = intent.getStringExtra("term_name");
            String title = intent.getStringExtra("end_alert_title");
            Integer alarmId = intent.getIntExtra("id", -1);
            NotificationCompat.Builder nb = notificationHelper.getChannel1Notifcation(title, courseName + " in " + termName);
            notificationHelper.getManager().notify(alarmId, nb.build());
        }
    }

    public static int getNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        return nextAlarmId;
    }

    public static void incrementNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        SharedPreferences.Editor alarmEditor = alarmPrefs.edit();
        alarmEditor.putInt(nextAlarmField, nextAlarmId + 1);
        alarmEditor.commit();
    }

    public static int getAndIncrementNextAlarmId(Context context) {
        int nextAlarmId = getNextAlarmId(context);
        incrementNextAlarmId(context);
        return nextAlarmId;
    }
}

