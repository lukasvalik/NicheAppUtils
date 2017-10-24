package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;
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

    public void askForPermission(final Activity context, int style) {
        if (!mPermissionInProgress) {
            mPermissionInProgress = true;
            AlertDialog.Builder builder = style == DEFAULT_STYLE ?
                    new AlertDialog.Builder(context) :
                    new AlertDialog.Builder(context, style);
            builder.setTitle(getPermissionTitle());
            builder.setMessage(getPermissionReason());
            builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
                mPermissionInProgress = false;
                onPermissionProceed(context);
            });
            builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> {
                mPermissionInProgress = false;
                onPermissionDenied();
            });
            builder.setCancelable(!isFatal());
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
        mPermissionManager.onPermissionDenied(getRequestCode());
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
