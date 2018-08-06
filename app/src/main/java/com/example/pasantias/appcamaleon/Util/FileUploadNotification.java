package com.example.pasantias.appcamaleon.Util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class FileUploadNotification {
    public static NotificationManager mNotificationManager;
    public static NotificationCompat.Builder builder;
    public static Context context;
    public static int NOTIFICATION_ID = 111;
    public static FileUploadNotification fileUploadNotification;

    public FileUploadNotification(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("start uploading")
                .setContentText("file name")
                .setSmallIcon(android.R.drawable.ic_media_pause)
                .setProgress(100, 0, false)
                .setAutoCancel(false);
    }

    public void alertNotification( String contentText,Context contextA) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(contextA)
                        .setSmallIcon(android.R.drawable.ic_media_pause)
                        .setContentTitle(contentText);

        NotificationManager mNotificationManager =
                (NotificationManager) contextA.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public void updateNotificatio(String percent, String fileName, String contentText) {
        try {
            builder.setContentTitle(fileName)
                    .setContentText(contentText + " " + percent + "%")
                    .setOngoing(true)
                    .setContentInfo(percent + "%")
                    .setProgress(100, Integer.parseInt(percent), false)
                    .setAutoCancel(false);
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
            if (Integer.parseInt(percent) > 100) {
                deleteNotification();
            }
        } catch (Exception e) {
            Log.e("Error ... Notification", e.getMessage() + "...");
            e.printStackTrace();
        }
    }

    public void failUploadNotification() {
        if (builder != null) {
            builder.setContentTitle("Uploading Failed")
                    .setContentText("Uploading...")
                    .setOngoing(true);
            //.setContentInfo(percent+"%")
            // .setProgress(100, Integer.parseInt(percent),false)
            // .setAutoCancel(false);
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
    }

    public void deleteNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
        builder = null;
    }
}
