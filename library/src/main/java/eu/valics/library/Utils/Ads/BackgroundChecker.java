package eu.valics.library.Utils.Ads;

import java.util.Timer;
import java.util.TimerTask;

import eu.valics.library.Base.BaseApplication;

/**
 * Created by L on 9/7/2016.
 */
public class BackgroundChecker {

    private static final long MAX_BETWEEN_ACTIVITY_TIME_MS = 2000;

    private Timer mTimer;
    private TimerTask mTimerTask;

    public void startBackgroundCheckerTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                BaseApplication.getInstance().getAppInfo().setGoInBackground(true);
            }
        };

        mTimer.schedule(mTimerTask, MAX_BETWEEN_ACTIVITY_TIME_MS);
    }

    public void stopBackgroundCheckerTimer() {

        if (mTimerTask != null)
            mTimerTask.cancel();
        if (mTimer != null)
            mTimer.cancel();

        BaseApplication.getInstance().getAppInfo().setGoInBackground(false);
    }
}
