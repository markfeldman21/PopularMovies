package com.markfeldman.popularmovies.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.content.ContextCompat;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.Sync.MovieIntentService;
import com.markfeldman.popularmovies.Sync.SyncHelper;
import com.markfeldman.popularmovies.activities.MainActivity;

/**
 * Created by markfeldman on 2/20/17.
 */

public class NotificationUtils {
    private final static int NOTIFICATION_ID = 334;
    private final static int NAVIGATE_TO_APP_PENDING_INTENT = 3;
    private final static int NOTIFICATION_MOVIE_ID = 34;
    private final static int IGNORE_PENDING_INTENT_ID = 8;
    private final static String NOTIFICATION_CHANNEL = "reminder_notification_channel";


    public static void notifyUser(Context context){
        Bitmap largeIcon = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.film);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL,context.getString(R.string.notifications_channel),NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_cam)
                .setLargeIcon(largeIcon)
                .setColor(ContextCompat.getColor(context,R.color.colorWhite))
                .setContentTitle(context.getString(R.string.notifications_title))
                .setContentText(context.getString(R.string.notifications_text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreNotification(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        notificationManager.notify(NOTIFICATION_MOVIE_ID,notificationBuilder.build());

        MovSharedPreferences.saveLastNotification(context, System.currentTimeMillis());

    }

    private static PendingIntent contentIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void clearAllNotificationServices(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static NotificationCompat.Action ignoreNotification(Context context){
        Intent intent = new Intent(context, MovieIntentService.class);
        intent.setAction(SyncHelper.DISMISS_ACTION_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(context,IGNORE_PENDING_INTENT_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Action(R.drawable.close,
                "NO THANKS",ignoreReminderPendingIntent);

    }
}
