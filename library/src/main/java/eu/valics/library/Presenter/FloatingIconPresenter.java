package eu.valics.library.Presenter;

import android.content.Context;
import android.content.Intent;

import eu.valics.library.AppInfo;
import eu.valics.library.FloatingIcon.AppNotification;
import eu.valics.library.FloatingIcon.FloatingIcon;
import eu.valics.library.FloatingIcon.FloatingIconService;

/**
 * Created by L on 9/7/2016.
 */
public class FloatingIconPresenter extends AdPresenter {

    public FloatingIconPresenter (Context context) {
        super(context);
    }

    public FloatingIconPresenter (Context context, AppInfo appInfo){
        super(context, appInfo);
    }

    @Override
    public void onResume(){
        super.onResume();
        putFloatingIconToNotification();
        startFloatingIconService();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void putFloatingIconToNotification() {
        if(FloatingIcon.get(mContext).getState() == FloatingIcon.SHOW_STATE) {
            AppNotification.get(mContext).createNotificationForFloatingIcon();
            FloatingIcon.get(mContext).setState(FloatingIcon.NOTIFICATION_STATE);
        }
    }

    public void startFloatingIconService() {
        if(!AppInfo.get(mContext).isMyServiceRunning(FloatingIconService.class)) {
            Intent intent = new Intent(mContext, FloatingIconService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startService(intent);
        }
    }
}
