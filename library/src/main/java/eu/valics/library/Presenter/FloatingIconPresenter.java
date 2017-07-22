package eu.valics.library.Presenter;

import android.content.Intent;

import eu.valics.library.Base.BaseApplication;
import eu.valics.library.FloatingIcon.AppNotification;
import eu.valics.library.FloatingIcon.FloatingIcon;
import eu.valics.library.FloatingIcon.FloatingIconService;

/**
 * Created by L on 9/7/2016.
 */
public class FloatingIconPresenter extends AdPresenter {

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
        if(!BaseApplication.getInstance().getAppInfo().isMyServiceRunning(FloatingIconService.class)) {
            Intent intent = new Intent(mContext, FloatingIconService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startService(intent);
        }
    }
}
