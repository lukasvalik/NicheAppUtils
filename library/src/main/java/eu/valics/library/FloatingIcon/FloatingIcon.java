package eu.valics.library.FloatingIcon;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import eu.valics.library.NicheAppUtils;

/**
 * Created by L on 9/7/2016.
 */
public class FloatingIcon extends android.support.v7.widget.AppCompatImageView {
    public static final int SHOW_STATE = 1;
    public static final int NOTIFICATION_STATE = 0;

    private static FloatingIcon sFloatingIcon;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private int state;

    private FloatingIcon(Context context) {
        this(context, null);
    }

    public FloatingIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloatingIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setImageResource(NicheAppUtils.getFloatingIconId());

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                                WindowManager.LayoutParams.TYPE_PHONE :
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        //params.y = 100;

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            windowManager.addView(this, params);

            setState(NOTIFICATION_STATE);
            AppNotification.get(context).createNotificationForFloatingIcon();

        } else if (Settings.canDrawOverlays(context)) {
            windowManager.addView(this, params);

            setState(NOTIFICATION_STATE);
            AppNotification.get(context).createNotificationForFloatingIcon();
        }
    }

    public static FloatingIcon get(Context context) {
        if (sFloatingIcon == null)
            sFloatingIcon = new FloatingIcon(context.getApplicationContext());
        return sFloatingIcon;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;

        setVisibility(state == SHOW_STATE ? VISIBLE : GONE);
        setEnabled(state == SHOW_STATE);
    }
}
