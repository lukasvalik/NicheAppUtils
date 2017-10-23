package eu.valics.library.Utils.permissionmanagement.Permission;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.R;
import eu.valics.library.Utils.permissionmanagement.SettingsPermission;

/**
 * Created by L on 10/23/2017.
 */

public class OverlayDrawPermission extends SettingsPermission {

    public OverlayDrawPermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    public boolean isEnabled(Context context) {
        return isPermitted(context);
    }

    @Override
    protected int getPermissionTitle() {
        return R.string.draw_overlay_title;
    }

    @Override
    protected int getPermissionReason() {
        return R.string.draw_overlay_message;
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.DRAW_OVERLAY;
    }

    @Override
    protected String getTitle() {
        return "Draw overlay";
    }

    public static boolean isPermitted(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(context);
    }
}
