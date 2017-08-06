package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 8/6/2017.
 */

public class ReadExternalStoragePermission extends DangerousPermission {

    public ReadExternalStoragePermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.READ_EXTERNAL_STORAGE;
    }

    @Override
    protected String getTitle() {
        return "Read External Storage";
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.READ_EXTERNAL_STORAGE;
    }
}
