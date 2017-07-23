package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 7/23/2017.
 */

public class AccessCoarseLocation extends DangerousPermission {

    public AccessCoarseLocation(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected String getTitle() {
        return "Internet Gps";
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.ACCESS_COURSE_LOCATION;
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.ACCESS_COARSE_LOCATION;
    }
}
