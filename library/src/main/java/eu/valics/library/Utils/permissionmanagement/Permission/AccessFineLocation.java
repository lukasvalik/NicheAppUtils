package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 7/23/2017.
 */

public class AccessFineLocation extends DangerousPermission {

    public AccessFineLocation(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected String getTitle() {
        return "Gps provider";
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.ACCESS_FINE_LOCATION;
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.ACCESS_FINE_LOCATION;
    }
}
