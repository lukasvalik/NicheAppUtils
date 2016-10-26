package eu.valics.library.FloatingIcon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import eu.valics.library.NicheAppUtils;

/**
 * Created by L on 9/7/2016.
 */
public class AppNotification {

    private int mIconId;

    private Context mContext;
    private static AppNotification sAppNotification;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mManager;

    private AppNotification(Context context){
        mContext = context;
        mManager= (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mIconId = NicheAppUtils.getFloatingIconId();
    }

    public static AppNotification get(Context context){
        if(sAppNotification == null)
            sAppNotification = new AppNotification(context.getApplicationContext());
        return sAppNotification;
    }

    public void createNotificationForFloatingIcon(){

        Intent i = new Intent(mContext, FloatingIconService.class);
        i.putExtra("EXTRA", "EXTRA");
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(mIconId)
                .setLargeIcon(createBitmapFromDrawable(mIconId))
                .setContentTitle(NicheAppUtils.getFloatingNotificationTitle())
                .setContentIntent(pendingIntent)
                .setContentText(NicheAppUtils.getFloatingNotificationDescription());

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mManager.notify(NicheAppUtils.getFloatingIconId(), notification);

    }

    public void hideNotification() {
        mManager.cancel(NicheAppUtils.getFloatingIconId());
    }

    private Bitmap createBitmapFromDrawable(int drawableId){
        return BitmapFactory.decodeResource(mContext.getResources(), drawableId);
    }
}
