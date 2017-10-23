package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;

import eu.valics.library.Base.AppInfo;

/**
 * Created by L on 7/7/2017.
 */

public abstract class DangerousPermission extends BasePermission {

    public DangerousPermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    void askForPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{getManifestPermission()}, getRequestCode());
        }
    }

    @Override
    public boolean isEnabled(Context context) {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context.checkSelfPermission(getManifestPermission()) != PackageManager.PERMISSION_GRANTED);
    }

    protected abstract int getRequestCode();

    protected abstract String getManifestPermission();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected boolean shouldAskRationale(Activity activity) {
        return activity.shouldShowRequestPermissionRationale(getManifestPermission());
    }

    @Override
    protected boolean wasAskedFirstTime(AppInfo appInfo) {
        return appInfo.wasAskedPermission(getRequestCode());
    }

    @Override
    protected boolean shouldNeverAskAgain(Activity activity) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (wasAskedFirstTime(mAppInfo) && !shouldAskRationale(activity));
    }

    private void showSettingsDialog(final Activity activity) {
        String message = "Open Settings, then tap Permissions and turn on " + getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton("Open Settings", (dialog, which) -> activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getApplicationContext().getPackageName()))));
        builder.setNegativeButton("Cancel", (dialog, which) -> mPermissionManager.onPermissionNotGranted(getRequestCode()));
        builder.show();
    }
}
