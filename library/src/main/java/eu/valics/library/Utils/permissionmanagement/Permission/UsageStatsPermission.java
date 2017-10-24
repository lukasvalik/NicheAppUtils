package eu.valics.library.Utils.permissionmanagement.Permission;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.R;
import eu.valics.library.Utils.permissionmanagement.SettingsPermission;

/**
 * Created by L on 10/23/2017.
 */

public class UsageStatsPermission extends SettingsPermission {

    public UsageStatsPermission(AppInfo appInfo) {
        super(appInfo);
        minVersion = Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public boolean isEnabled(Context context) {
        return isPermitted(context);
    }

    @Override
    protected int getPermissionTitle() {
        return R.string.usage_stats_title;
    }

    @Override
    protected int getPermissionReason() {
        return R.string.usage_stats_reason;
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.USAGE_STATS;
    }

    @Override
    protected String getTitle() {
        return "Usage Stats";
    }

    @Override
    protected void onPermissionProceed(Context context) {
        super.onPermissionProceed(context);
        context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    public static boolean isPermitted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        } else
            return true;
    }
}
