package eu.valics.library.Utils.Ads;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

import eu.valics.library.AppInfo;

/**
 * Created by L on 9/7/2016.
 */
public class BackgroundChecker {

    private static final long MAX_BETWEEN_ACTIVITY_TIME_MS = 2000;

    private static BackgroundChecker sBackgroundChecker;
    private Context mContext;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private BackgroundChecker(Context context) {
        mContext = context;
    }

    public static BackgroundChecker get(Context context){
        if (sBackgroundChecker == null)
            sBackgroundChecker = new BackgroundChecker(context.getApplicationContext());
        return sBackgroundChecker;
    }

    public void startBackgroundCheckerTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                AppInfo.get(mContext).setGoInBackground(true);
            }
        };

        mTimer.schedule(mTimerTask, MAX_BETWEEN_ACTIVITY_TIME_MS);
    }

    public void stopBackgroundCheckerTimer() {

        if (mTimerTask != null)
            mTimerTask.cancel();
        if (mTimer != null)
            mTimer.cancel();

        AppInfo.get(mContext).setGoInBackground(false);
    }
}
