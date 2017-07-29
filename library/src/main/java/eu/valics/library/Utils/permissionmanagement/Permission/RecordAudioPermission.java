package eu.valics.library.Utils.permissionmanagement.Permission;

import android.Manifest;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Utils.permissionmanagement.DangerousPermission;

/**
 * Created by L on 7/29/2017.
 */

public class RecordAudioPermission extends DangerousPermission {

    public RecordAudioPermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected String getTitle() {
        return "Record Audio";
    }

    @Override
    protected int getRequestCode() {
        return RequestCode.RECORD_AUDIO;
    }

    @Override
    protected String getManifestPermission() {
        return Manifest.permission.RECORD_AUDIO;
    }
}
