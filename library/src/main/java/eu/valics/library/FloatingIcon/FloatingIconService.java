package eu.valics.library.FloatingIcon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import eu.valics.library.AppInfo;
import eu.valics.library.NicheAppUtils;
import eu.valics.library.SplashActivityLauncher;

/**
 * Created by L on 9/7/2016.
 */
public class FloatingIconService extends Service {

    private FloatingIcon mFloatingIcon;
    private int clickCounter;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mFloatingIcon == null)
            initFloatingIcon();

        if (intent != null) {
            if (intent.getExtras() != null) {
                mFloatingIcon.setState(FloatingIcon.SHOW_STATE);
                AppNotification.get(this).hideNotification();
            } else {
                mFloatingIcon.setState(FloatingIcon.NOTIFICATION_STATE);
                AppNotification.get(this).createNotificationForFloatingIcon();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initFloatingIcon();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(NicheAppUtils.getFloatingServiceRestartPhrase());
            i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, i, 0);
            am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, pendingIntent);
        } else {
            Intent i = new Intent(NicheAppUtils.getFloatingServiceRestartPhrase());
            sendBroadcast(i);
        }
    }

    private void initFloatingIcon() {

        mFloatingIcon = FloatingIcon.get(this);

        clickCounter = 0;

        try {
            mFloatingIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickCounter++;
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            if (clickCounter == 1)
                                startApp();
                            else if (clickCounter == 2) {
                                mFloatingIcon.setState(FloatingIcon.NOTIFICATION_STATE);
                                AppNotification.get(getApplicationContext())
                                        .createNotificationForFloatingIcon();
                            }
                            clickCounter = 0;
                        }
                    };
                    if (clickCounter == 1) {
                        handler.postDelayed(r, 250);
                    }
                }
            });

        } catch (Exception e) {

        }

        try {
            mFloatingIcon.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = mFloatingIcon.getParams();
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            // Get current time in nano seconds.

                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            mFloatingIcon.getWindowManager()
                                    .updateViewLayout(mFloatingIcon, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void startApp() {
        AppInfo.get(this).setGoInBackground(true);
        AppInfo.get(this).setShowingInterstitialAd(false);

        SplashActivityLauncher.launch(this);
    }
}
