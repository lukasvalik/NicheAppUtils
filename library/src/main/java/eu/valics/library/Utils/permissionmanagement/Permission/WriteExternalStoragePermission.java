package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 8/6/2017.
 */

public class WriteExternalStoragePermission extends DangerousPermission {

    public WriteExternalStoragePermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.WRITE_EXTERNAL_STORAGE;
    }

    @Override
    protected String getTitle() {
        return "Write External Storage";
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.WRITE_EXTERNAL_STORAGE;
    }
}
