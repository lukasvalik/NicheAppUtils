package eu.valics.library.Utils.permissionmanagement.Permission;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.R;
import eu.valics.library.Utils.permissionmanagement.SettingsPermission;

/**
 * Created by L on 7/7/2017.
 */

public class NotificationAccessPermission extends SettingsPermission {

    public NotificationAccessPermission(AppInfo appInfo){
        super(appInfo);
    }

    @Override
    public boolean isEnabled(Context context) {
        try{
            if(Settings.Secure.getString(context.getContentResolver(),
                    "enabled_notification_listeners").contains(context.getApplicationContext().getPackageName()))
            {
                return true;
            } else {
                return false;
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.NOTIFICATION_ACCESS;
    }

    @Override
    protected String getTitle() {
        return "Notification Acccess";
    }

    @Override
    protected int getPermissionTitle() {
        return R.string.notification_access_title;
    }

    @Override
    protected int getPermissionReason() {
        return R.string.notification_access_message;
    }

    @Override
    protected void onPermissionProceed(Context context) {
        super.onPermissionProceed(context);
        context.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }
}
