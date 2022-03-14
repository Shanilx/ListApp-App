package com.listapp.FCM;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.listapp.Activity.NotificationActivity;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.util.List;
import java.util.Map;

//import android.support.v7.app.NotificationCompat;

/**
 * Created by Nivesh on 6/12/2017.
 */

public class NotificationFromFirebase extends FirebaseMessagingService {

    private String title;
    private String message;
    private String userID;
    private String date, time;
    private String id;
    private String type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            Map<String, String> params = remoteMessage.getData();
            title = params.get("alert");
            message = params.get("message");
            userID = params.get("notification_user_id");
            date = params.get("date");
            time = params.get("time");
            id = params.get("notification_id");
            type = params.get("notification_type");
            if (id != null) {

                Intent intent = null;
                intent = new Intent(this, NotificationActivity.class);
                int i = PreferenceConnector.readInteger(this, "notification_", 1);
                if (i == 1) {
                    i += 1;
                    PreferenceConnector.writeInteger(this, "notification_", i);
                } else {
                    i += 1;
                    PreferenceConnector.writeInteger(this, "notification_", i);
                }
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("msg", message);
                intent.putExtra("title", title);
                intent.putExtra("notification_id", id);
                intent.putExtra("type", type);


//                APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
//                String mobileNumber=PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.MOBILE_NUMBER,"");
//                String deviceType=PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.DEVICE_TYPE,"");
//                String deviceToken=PreferenceConnector.readString(getBaseContext(),PreferenceConnector.DEVICE_TOKEN,"");
//                userID=PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.USER_ID,"")
//                Call<NotificationModel> getProfileCall = apiInterface.postNotification(mobileNumber, deviceType, deviceToken,userID,message,id,title,type);
//                getProfileCall.enqueue(new Callback() {
//                    @Override
//                    public void onResponse(Call call, Response response) {
//                        Log.d("notification done","true");
//                    }
//
//                    @Override
//                    public void onFailure(Call call, Throwable t) {
//                        Log.d("notification done","false");
//                    }
//                });

                PendingIntent pendingIntent = PendingIntent.getActivity
                        (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = null;
                try {
                    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                NotificationCompat.Builder builder = new NotificationCompat
                        .Builder(getApplicationContext());

                builder.setContentTitle(title + "")
                        .setContentText(message + "is here")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.drawable.notification);
                    builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryYellow));
                } else {
                    builder.setSmallIcon(R.drawable.app_icon);
                }

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(i, builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
        PreferenceConnector.writeString(this,PreferenceConnector.DEVICE_TOKEN,s);

    }

    private boolean isAppIsInBackground() {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(this.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(this.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}

