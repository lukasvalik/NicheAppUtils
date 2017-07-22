package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import eu.valics.library.Base.AppPausingProcess;
import eu.valics.library.Base.AppInfo;
import eu.valics.library.R;

/**
 * Created by L on 7/7/2017.
 */

public class PermissionManager implements PermissionManagement, AppPausingProcess {

    private PermissionInvalidationListener mListener;

    private ArrayList<BasePermission> mPermissions;
    private ArrayList<PermissionGroup> mPermissionGroups;
    private Activity mActivity;
    private AppInfo mAppInfo;
    //private boolean mAskingFatalPermissionInProgress = false;
    private BasePermission mPermissionInProgress;

    private PermissionManager(Activity activity,
                              AppInfo appInfo,
                              ArrayList<BasePermission> permissions,
                              ArrayList<PermissionGroup> permissionGroups) {
        mPermissions = permissions;
        mPermissionGroups = permissionGroups;
        mActivity = activity;
        mAppInfo = appInfo;

        subscribePermissionManagerToSettingsPermissions();
    }

    /**
     * We put reference inside all SettingsPermissions because they do not trigger callback in activity
     */

    private void subscribePermissionManagerToSettingsPermissions() {
        for (BasePermission permission : mPermissions) {
            if (permission instanceof SettingsPermission)
                ((SettingsPermission) permission).setPermissionManager(this);
        }
        for (PermissionGroup group : mPermissionGroups) {
            for (BasePermission permission : group.getPermissions()) {
                if (permission instanceof SettingsPermission)
                    ((SettingsPermission) permission).setPermissionManager(this);
            }
        }
    }

    /**
     * invalidate during on resume is good to not forget on any of permissions.
     * It also takes our second ask for the fatal permission
     *
     * @param ignoreDeniedFlag
     */

    public void invalidatePermissions(boolean ignoreDeniedFlag) {
        //if (mAskingFatalPermissionInProgress) return;
        if (mPermissionInProgress != null) return;
        if (ignoreDeniedFlag) {
            for (BasePermission permission : mPermissions) {
                if (!permission.isEnabled(mActivity) && !permission.shouldNeverAskAgain(mActivity)) {
                    permission.askForPermission(mActivity);
                    mAppInfo.setAskedPermission(permission.getRequestCode());
                    mPermissionInProgress = permission;
                    return;
                } else if (!permission.isEnabled(mActivity) && permission.shouldNeverAskAgain(mActivity) && permission.isFatal()) {
                    showSettingsDialog(mActivity, permission.getTitle());
                    mPermissionInProgress = permission;
                    return;
                } else {
                    permission.setDenied(true);
                }
            }
            if (mPermissionGroups.size() > 0) {
                for (PermissionGroup permissionGroup : mPermissionGroups) {
                    ArrayList<BasePermission> permissions = permissionGroup.getPermissions();
                    for (int i = 0; i < permissions.size(); i++) {
                        BasePermission permission = permissions.get(i);
                        if (!permission.isEnabled(mActivity) && !permission.shouldNeverAskAgain(mActivity)) {
                            permission.askForPermission(mActivity);
                            mAppInfo.setAskedPermission(permission.getRequestCode());
                            mPermissionInProgress = permission;
                            return;
                        } else if (!permission.isEnabled(mActivity) && permission.shouldNeverAskAgain(mActivity) && permission.isFatal()) {
                            showSettingsDialog(mActivity, permission.getTitle());
                            mPermissionInProgress = permission;
                            return;
                        } else {
                            permission.setDenied(true);
                        }
                    }
                }
            }
        } else {
            for (BasePermission permission : mPermissions) {
                if (!permission.isEnabled(mActivity) && !permission.isDenied() && !permission.shouldNeverAskAgain(mActivity)) {
                    permission.askForPermission(mActivity);
                    mAppInfo.setAskedPermission(permission.getRequestCode());
                    mPermissionInProgress = permission;
                    return;
                } else if (!permission.isEnabled(mActivity) && permission.shouldNeverAskAgain(mActivity) && permission.isFatal()) {
                    showSettingsDialog(mActivity, permission.getTitle());
                    mPermissionInProgress = permission;
                    return;
                } else {
                    permission.setDenied(true);
                }
            }
            if (mPermissionGroups.size() > 0) {
                for (PermissionGroup permissionGroup : mPermissionGroups) {
                    if (!permissionGroup.isDenied()) {
                        ArrayList<BasePermission> permissions = permissionGroup.getPermissions();
                        for (int i = 0; i < permissions.size(); i++) {
                            BasePermission permission = permissions.get(i);
                            if (!permission.isEnabled(mActivity) && !permission.isDenied() && !permission.shouldNeverAskAgain(mActivity)) {
                                permission.askForPermission(mActivity);
                                mAppInfo.setAskedPermission(permission.getRequestCode());
                                //mAskingFatalPermissionInProgress = true;
                                mPermissionInProgress = permission;
                                return;
                            } else if (!permission.isEnabled(mActivity) && permission.shouldNeverAskAgain(mActivity) && permission.isFatal()) {
                                showSettingsDialog(mActivity, permission.getTitle());
                                mPermissionInProgress = permission;
                                return;
                            } else {
                                permission.setDenied(true);
                            }
                        }
                    }
                }
            }
        }
        if (mListener != null) mListener.onPermissionInvalidated();
    }

    private PermissionGroup getPermissionGroupByTitle(String permissionGroupTitle) {
        for (PermissionGroup permissionGroup : mPermissionGroups) {
            if (permissionGroup.getTitle().equals(permissionGroupTitle))
                return permissionGroup;
        }
        throw new IllegalArgumentException("Wrong permissionGroup title");
    }

    /**
     * This could be done on next start. If user is not willing to give permission once, we ask next time
     * This resets flag to enable asking in the same session. Eg user tryes to start service again, so
     * we want to give him option to enable permissions needed to start it
     */

    public void resetPermissionDeniedFlag() {
        for (BasePermission permission : mPermissions) {
            permission.setDenied(false);
        }
        for (PermissionGroup group : mPermissionGroups) {
            group.setDenied(false);
            for (BasePermission permission : group.getPermissions()) {
                permission.setDenied(false);
            }
        }
    }

    /**
     * @return whether user enabled at least one permission. At least one functionality will work, he can turn on this feature
     */

    public boolean enabledAtLeastOne() {
        for (BasePermission permission : mPermissions) {
            if (permission.isEnabled(mActivity)) return true;
        }
        return false;
    }

    /**
     * @return check whether all permissions are granted
     */

    public boolean enabledAllPermissions() {
        for (BasePermission permission : mPermissions) {
            if (!permission.isEnabled(mActivity)) return false;
        }
        return true;
    }

    private BasePermission getPermissionByRequestCode(int requestCode) {
        for (BasePermission permission : mPermissions) {
            if (permission.getRequestCode() == requestCode) return permission;
        }
        for (PermissionGroup permissionGroup : mPermissionGroups) {
            for (BasePermission permission : permissionGroup.getPermissions()) {
                if (permission.getRequestCode() == requestCode) return permission;
            }
        }
        throw new IllegalArgumentException("Not Correct RequestCode");
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        //mAskingFatalPermissionInProgress = false;
        mPermissionInProgress = null;
        invalidatePermissions(false);
    }

    @Override
    public void onPermissionNotGranted(final int requestCode) {
        //mAskingFatalPermissionInProgress = false;
        mPermissionInProgress = null;
        BasePermission permission = getPermissionByRequestCode(requestCode);
        PermissionGroup permissionGroup = getPermissionGroup(permission);
        if (permissionGroup != null && permissionGroup.isFatal() && permissionGroup.allOtherPermissionsDenied(permission)) {
            showFatalDialog(permission, permissionGroup);
        } else if (permission.isFatal()) {
            showFatalDialog(permission, permissionGroup);
        } else {
            permission.setDenied(true);
            if (permissionGroup != null && permissionGroup.allOtherPermissionsDenied(permission)) {
                permissionGroup.setDenied(true);
            }
        }

    }

    private PermissionGroup getPermissionGroup(BasePermission permission) {
        for (PermissionGroup permissionGroup : mPermissionGroups) {
            for (BasePermission basePermission : permissionGroup.getPermissions()) {
                if (basePermission.getRequestCode() == permission.getRequestCode())
                    return permissionGroup;
            }
        }
        return null;
    }

    @Override
    public void onPermissionAccepted(int requestCode) {

    }

    @Override
    public void onPermissionDenied(int requestCode) {
        BasePermission permission = getPermissionByRequestCode(requestCode);
        PermissionGroup permissionGroup = getPermissionGroup(permission);
        if (permissionGroup != null && permissionGroup.isFatal() && permissionGroup.allOtherPermissionsDenied(permission)) {
            showFatalDialog(permission, permissionGroup);
        } else if (permission.isFatal()) {
            showFatalDialog(permission, permissionGroup);
        } else {
            permission.setDenied(true);
            if (permissionGroup != null && permissionGroup.allOtherPermissionsDenied(permission)) {
                permissionGroup.setDenied(true);
            }
        }
    }

    public boolean isEnabledPermission(int requestCode) {
        for (BasePermission permission : mPermissions) {
            if (permission.getRequestCode() == requestCode)
                return true;
        }
        for (PermissionGroup permissionGroup : mPermissionGroups) {
            for (BasePermission basePermission : permissionGroup.getPermissions()) {
                if (basePermission.getRequestCode() == requestCode)
                    return true;
            }
        }
        return false;
    }

    public boolean isEnabledGroup(Context context, String title) {
        for (PermissionGroup permissionGroup : mPermissionGroups) {
            if (permissionGroup.getTitle().equals(title) && permissionGroup.isEnabled(context))
                return true;
        }
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean finish() {
        invalidatePermissions(false);
        return mPermissionInProgress == null;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    public static class Builder {
        private ArrayList<BasePermission> permissions;
        private ArrayList<PermissionGroup> permissionGroups;
        private Activity activity;
        private AppInfo appInfo;

        public Builder() {
            permissions = new ArrayList<>();
            permissionGroups = new ArrayList<>();
        }

        public Builder with(Activity activity, AppInfo appInfo) {
            this.activity = activity;
            this.appInfo = appInfo;
            return this;
        }

        public Builder addPermission(BasePermission permission) {
            permissions.add(permission);
            return this;
        }

        public Builder addPermissionGroup(PermissionGroup permissionGroup) {
            permissionGroups.add(permissionGroup);
            return this;
        }

        public PermissionManager build() {
            if (activity == null) throw new IllegalArgumentException("Activity cannot be null");
            return new PermissionManager(activity, appInfo, permissions, permissionGroups);
        }
    }

    private void showFatalDialog(final BasePermission permission, final PermissionGroup permissionGroup) {
        //mAskingFatalPermissionInProgress = true;
        mPermissionInProgress = permission;
        String title = permissionGroup != null ? permissionGroup.getFatalDialogTitle() : permission.getFatalDialogTitle();
        String message = permissionGroup != null ? permissionGroup.getFatalDialogMessage() : permission.getFatalDialogMessage();
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mAskingFatalPermissionInProgress = false;
                mPermissionInProgress = null;
                if (permissionGroup != null) permissionGroup.resetAllDeniedStatuses();
                invalidatePermissions(false);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mAskingFatalPermissionInProgress = false;
                mPermissionInProgress = null;
                permission.setDenied(true);
                if (permissionGroup != null) permissionGroup.setDenied(true);
                //invalidatePermissions(false); // no need to ask anymore permissions
            }
        });
        builder.show();
    }

    private void showSettingsDialog(final Activity activity, String permissionTitle) {
        //mAskingFatalPermissionInProgress = true;
        String message = "Open Settings, then tap Permissions and turn on " + permissionTitle;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mAskingFatalPermissionInProgress = false;
                mPermissionInProgress = null;
                activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getApplicationContext().getPackageName())));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPermissionInProgress = null;
                //mAskingFatalPermissionInProgress = false;
            }
        });
        builder.show();
    }

    public PermissionInvalidationListener getListener() {
        return mListener;
    }

    public void setListener(PermissionInvalidationListener listener) {
        mListener = listener;
    }

    public BasePermission getPermissionInProgress() {
        return mPermissionInProgress;
    }

    public void setPermissionInProgress(BasePermission permissionInProgress) {
        mPermissionInProgress = permissionInProgress;
    }
}