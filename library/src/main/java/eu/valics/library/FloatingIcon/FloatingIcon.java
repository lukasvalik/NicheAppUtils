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
public class FloatingIcon extends ImageView {
    public static final int SHOW_STATE = 1;
    public static final int NOTIFICATION_STATE = 0;

    private static FloatingIcon sFloatingIcon;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private int state;

    private FloatingIcon(Context context) {
        super(context);

        setImageResource(NicheAppUtils.getFloatingIconId());

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        //params.y = 100;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.canDrawOverlays(context))
                windowManager.addView(this, params);
        } else {
            windowManager.addView(this, params);
        }

        setState(NOTIFICATION_STATE);
        AppNotification.get(context).createNotificationForFloatingIcon();
    }

    public FloatingIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static FloatingIcon get(Context context){
        if(sFloatingIcon == null)
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

        if(state == SHOW_STATE) {
            setVisibility(VISIBLE);
            setEnabled(true);
        }
        if(state == NOTIFICATION_STATE) {
            setVisibility(GONE);
            setEnabled(false);
        }
    }
}
