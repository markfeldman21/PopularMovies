package com.markfeldman.popularmovies.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
    private final static int IGNORE_PENDING_INTENT_ID = 8;


    public static void notifyUser(Context context){
        Log.v("TAG","IN NOTIFICATIONS++++++");
        String notificationTitle = "MOVIES";
        String notificationText = "Check out new and most popular movies out now!";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.play_button)
                .setContentTitle(context.getString(R.string.notifications_title))
                .setContentText(context.getString(R.string.notifications_text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreNotification(context))
                .setAutoCancel(true);

        /*
        Intent openMainPageApp = new Intent(context, MainActivity.class);
        //Create proper pending intent using TaskStackBuilder
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(openMainPageApp);
        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        */


        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(34,notificationBuilder.build());

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

    public static NotificationCompat.Action checkOutApp(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent navigateToAppPendingIntent = PendingIntent.getService(context,NAVIGATE_TO_APP_PENDING_INTENT,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action navigate = new NotificationCompat.Action(R.drawable.check,
                "I DID IT",navigateToAppPendingIntent);
        return navigate;
    }
}
