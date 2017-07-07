package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.AppInfo;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 7/7/2017.
 */

public class ReadPhoneStatePermission extends DangerousPermission {
    public ReadPhoneStatePermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.READ_PHONE_STATE;
    }

    @Override
    protected String getTitle() {
        return "Read Phone State";
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.READ_PHONE_STATE;
    }
}
