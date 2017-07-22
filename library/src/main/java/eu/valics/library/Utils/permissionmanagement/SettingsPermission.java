package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.R;

/**
 * Created by L on 7/7/2017.
 */

public abstract class SettingsPermission extends BasePermission {

    private boolean mPermissionInProgress = false;
    protected PermissionManager mPermissionManager;

    public SettingsPermission(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    public void askForPermission(final Activity context) {
        if (!mPermissionInProgress) {
            mPermissionInProgress = true;
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);
            builder.setTitle(getPermissionTitle());
            builder.setMessage(getPermissionReason());
            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPermissionInProgress = false;
                    onPermissionProceed(context);
                }
            });
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPermissionInProgress = false;
                    onPermissionDenied();
                }
            });
            builder.show();
        }
    }

    public abstract boolean isEnabled(Context context);

    protected abstract int getPermissionTitle();

    protected abstract int getPermissionReason();

    // this permissions should send user to correct settings page.
    // No need to make it public
    protected void onPermissionProceed(Context context) {
        mPermissionManager.setPermissionInProgress(null);
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        mPermissionManager = permissionManager;
    }

    protected void onPermissionDenied() {
        mDenied = true;
        mPermissionManager.onPermissionDenied(RequestCode.NOTIFICATION_ACCESS);
        mPermissionManager.setPermissionInProgress(null);
    }

    @Override
    protected boolean shouldAskRationale(Activity activity) {
        return false;
    }

    @Override
    protected boolean wasAskedFirstTime(AppInfo appInfo) {
        return false;
    }

    @Override
    protected boolean shouldNeverAskAgain(Activity activity) {
        return false;
    }
}
