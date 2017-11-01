package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.R;
import eu.valics.library.View.SettingsPermissionDialog;

/**
 * Created by L on 7/7/2017.
 */

public abstract class SettingsPermission extends BasePermission {

    private boolean mPermissionInProgress = false;
    protected PermissionManager mPermissionManager;
    private Drawable descriptionDrawable;
    private Drawable buttonBackgroundDrawable;

    public SettingsPermission(AppInfo appInfo) {
        super(appInfo);
    }

    public SettingsPermission(AppInfo appInfo, Drawable descriptionDrawable, Drawable buttonBackgroundDrawable) {
        super(appInfo);
        this.descriptionDrawable = descriptionDrawable;
        this.buttonBackgroundDrawable = buttonBackgroundDrawable;
    }

    public void askForPermission(final Activity context, int style) {
        if (!mPermissionInProgress) {
            mPermissionInProgress = true;

            if (descriptionDrawable != null && buttonBackgroundDrawable != null) {

                SettingsPermissionDialog settingsPermission =
                        new SettingsPermissionDialog.Builder()
                                .with(context)
                                .style(style)
                                .setTitle(context.getString(getPermissionTitle()))
                                .setMessage(context.getString(getPermissionReason()))
                                .setImageDescription(descriptionDrawable)
                                .setButtonBackground(buttonBackgroundDrawable)
                                .setPositiveButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mPermissionInProgress = false;
                                        onPermissionProceed(context);
                                    }
                                })
                                .build();
                settingsPermission.setCancelable(false);
                settingsPermission.show();
            } else {
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
