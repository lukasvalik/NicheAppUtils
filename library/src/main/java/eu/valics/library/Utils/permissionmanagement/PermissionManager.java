package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Base.BaseActivity;
import eu.valics.library.R;
import io.reactivex.Observable;

/**
 * Created by L on 7/7/2017.
 */

public class PermissionManager implements PermissionManagement {

    private PermissionInvalidationListener mListener;

    private ArrayList<BasePermission> mPermissions;
    private ArrayList<PermissionGroup> mPermissionGroups;
    private BaseActivity mActivity;
    private AppInfo mAppInfo;
    private BasePermission mPermissionInProgress;

    private PermissionManager(BaseActivity activity,
                              AppInfo appInfo,
                              ArrayList<BasePermission> permissions,
                              ArrayList<PermissionGroup> permissionGroups) {
        mPermissions = permissions;
        mPermissionGroups = permissionGroups;
        mActivity = activity;
        mAppInfo = appInfo;

        subscribePermissionManagerToPermissions();
    }

    /**
     * We put reference inside all SettingsPermissions because they do not trigger callback in activity
     */

    private void subscribePermissionManagerToPermissions() {
        //Observable.fromIterable(mPermissions).doOnNext(p -> p.setPermissionManager(this));
        //Observable.fromIterable(mPermissionGroups).doOnNext(g -> Observable.fromIterable(g.getPermissions()).doOnNext(p -> p.setPermissionManager(this)));

        for (BasePermission permission : mPermissions) {
            permission.setPermissionManager(this);
        }
        for (PermissionGroup group : mPermissionGroups) {
            for (BasePermission perm: group.getPermissions()) {
                perm.setPermissionManager(this);
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
                    showSettingsDialog(mActivity, permission);
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
                        } else if (!permission.isEnabled(mActivity) && permission.shouldNeverAskAgain(mActivity) && permissionGroup.isFatal()) {
                            showSettingsDialog(mActivity, permission);
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
                    showSettingsDialog(mActivity, permission);
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
                            } else if (!permission.isEnabled(mActivity) && permission.shouldNeverAskAgain(mActivity) && permissionGroup.isFatal()) {
                                showSettingsDialog(mActivity, permission);
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

    private PermissionGroup findPermissionGroup(String permissionGroupTitle) {
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
     * @return check whether all fatal permissions are granted
     */

    public boolean enabledAllFatalPermissions() {
        return Observable.fromIterable(mPermissions).filter(p->p.isFatal() && !p.isEnabled(mActivity)).count().blockingGet() == 0 &&
                Observable
                        .fromIterable(mPermissionGroups)
                        .filter(group-> group.isFatal() && group.getPermissions().size() > 0 && Observable.fromIterable(group.getPermissions()).filter(p -> p.isEnabled(mActivity)).count().blockingGet() == 0)
                        .count()
                        .blockingGet() == 0;
    }

    private BasePermission findPermission(int requestCode) {
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

    private PermissionGroup findPermissionGroup(BasePermission permission) {
        for (PermissionGroup permissionGroup : mPermissionGroups) {
            for (BasePermission basePermission : permissionGroup.getPermissions()) {
                if (basePermission.getRequestCode() == permission.getRequestCode())
                    return permissionGroup;
            }
        }
        return null;
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
        BasePermission permission = findPermission(requestCode);
        PermissionGroup permissionGroup = findPermissionGroup(permission);
        if (permissionGroup != null && permissionGroup.isFatal() && permissionGroup.allOtherPermissionsDenied(permission)) {
            showFatalDialog(permission, permissionGroup);
        } else if (permission.isFatal()) {
            showFatalDialog(permission, permissionGroup);
        } else {
            permission.setDenied(true);
            if (permissionGroup != null && permissionGroup.allOtherPermissionsDenied(permission)) {
                permissionGroup.setDenied(true);
            }
            mActivity.invalidateAppPausingProcesses();
        }
    }

    @Override
    public void onPermissionAccepted(int requestCode) {

    }

    @Override
    public void onPermissionDenied(int requestCode) {
        BasePermission permission = findPermission(requestCode);
        PermissionGroup permissionGroup = findPermissionGroup(permission);
        if (permissionGroup != null && permissionGroup.isFatal() && permissionGroup.allOtherPermissionsDenied(permission) && !permissionGroup.enabledAtLeastOnePermission(mActivity)) {
            showFatalDialog(permission, permissionGroup);
        } else if (permission.isFatal()) {
            showFatalDialog(permission, permissionGroup);
        } else {
            permission.setDenied(true);
            if (permissionGroup != null && permissionGroup.allOtherPermissionsDenied(permission)) {
                permissionGroup.setDenied(true);
            }
            mPermissionInProgress = null;
            mActivity.invalidateAppPausingProcesses();
        }
    }

    public boolean isEnabledPermission(int requestCode) {
        return isPermissionEnabledFromList(mPermissions, requestCode) ||
                Observable.fromIterable(mPermissionGroups)
                        .filter(group -> isPermissionEnabledFromList(group.getPermissions(), requestCode))
                        .count()
                        .blockingGet() > 0;
    }

    public boolean isPermissionEnabledFromList(ArrayList<BasePermission> permissions, int requestCode) {
        return Observable.fromIterable(permissions).filter(p->p.getRequestCode() == requestCode && p.isEnabled(mActivity)).count().blockingGet() > 0;
    }

    public boolean isEnabledGroup(String title) {
        return Observable.fromIterable(mPermissionGroups).filter(g-> g.getTitle().equals(title) && g.isEnabled(mActivity)).count().blockingGet() > 0;
    }

    public static class Builder {
        private ArrayList<BasePermission> permissions;
        private ArrayList<PermissionGroup> permissionGroups;
        private BaseActivity activity;
        private AppInfo appInfo;

        public Builder() {
            permissions = new ArrayList<>();
            permissionGroups = new ArrayList<>();
        }

        public Builder with(BaseActivity activity, AppInfo appInfo) {
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
        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            //mAskingFatalPermissionInProgress = false;
            mPermissionInProgress = null;
            if (permissionGroup != null) permissionGroup.resetAllDeniedStatuses();
            invalidatePermissions(false);
        });
        builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> {
            //mAskingFatalPermissionInProgress = false;
            mPermissionInProgress = null;
            permission.setDenied(true);
            if (permissionGroup != null) permissionGroup.setDenied(true);
            if (mListener != null) mListener.onPermissionInvalidated();
            //invalidatePermissions(false); // no need to ask anymore permissions
        });
        builder.show();
    }

    private void showSettingsDialog(final Activity activity, BasePermission permission) {
        //mAskingFatalPermissionInProgress = true;
        String message = "Open Settings, then tap Permissions and turn on " + permission.getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton("Open Settings", (dialog, which) -> {
            mPermissionInProgress = null;
            activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getApplicationContext().getPackageName())));
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            onPermissionNotGranted(permission.getRequestCode());
            mPermissionInProgress = null;
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

    public AppInfo getAppInfo() {
        return mAppInfo;
    }
}
