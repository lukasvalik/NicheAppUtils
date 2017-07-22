package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Utils.permissionmanagement.BasePermission;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 7/7/2017.
 */

public class CameraPermission extends DangerousPermission {

    public CameraPermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected int getRequestCode() {
        return BasePermission.RequestCode.CAMERA;
    }

    @Override
    protected String getTitle() {
        return "Camera";
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.CAMERA;
    }
}
