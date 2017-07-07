package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.AppInfo;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 7/7/2017.
 */

public class ReceiveSmsPermission extends DangerousPermission {

    public ReceiveSmsPermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.RECEIVE_SMS;
    }

    @Override
    protected String getTitle() {
        return "Receive SMS";
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.RECEIVE_SMS;
    }
}
