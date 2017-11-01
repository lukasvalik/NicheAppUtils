package eu.valics.library.Utils.permissionmanagement.Permission;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

    public OverlayDrawPermission(AppInfo appInfo, Drawable descriptionDrawable, Drawable buttonBackgroundDrawable) {
        super(appInfo, descriptionDrawable, buttonBackgroundDrawable);
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

    @Override
    protected void onPermissionProceed(Context context) {
        super.onPermissionProceed(context);
        context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())));
    }

    public static boolean isPermitted(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(context);
    }
}
