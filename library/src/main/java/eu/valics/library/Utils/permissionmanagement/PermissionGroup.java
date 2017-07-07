package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by L on 7/7/2017.
 */

public class PermissionGroup {

    private String mTitle;
    private ArrayList<BasePermission> mPermissions;
    private boolean mFatal;
    private boolean mDenied;
    private String mFatalDialogTitle;
    private String mFatalDialogMessage;

    private PermissionGroup(String title, ArrayList<BasePermission> basePermissions, boolean fatal) {
        mPermissions = basePermissions;
        mFatal = fatal;
        mTitle = title;
    }

    public ArrayList<BasePermission> getPermissions() {
        return mPermissions;
    }

    public boolean isFatal() {
        return mFatal;
    }

    public void setFatal(boolean fatal, String fatalDialogTitle, String fatalDialogMessage) {
        mFatal = fatal;
        mFatalDialogTitle = fatalDialogTitle;
        mFatalDialogMessage = fatalDialogMessage;
    }

    public String getFatalDialogTitle() {
        return mFatalDialogTitle;
    }

    public String getFatalDialogMessage() {
        return mFatalDialogMessage;
    }

    public boolean isEnabled(Context context) {
        return enabledAtLeastOnePermission(context);
    }

    private boolean enabledAtLeastOnePermission(Context context) {
        for (BasePermission permission : mPermissions) {
            if (permission.isEnabled(context)) return true;
        }
        return false;
    }

    public boolean isDenied() {
        return mDenied;
    }

    public void setDenied(boolean denied) {
        mDenied = denied;
    }

    public boolean allOtherPermissionsDenied(BasePermission permission) {
        for (BasePermission basePermission : mPermissions) {
            if (basePermission.getRequestCode() != permission.getRequestCode() && !basePermission.isDenied())
                return false;
        }
        return true;
    }

    public void resetAllDeniedStatuses() {
        mDenied = false;
        for (BasePermission permission : mPermissions) {
            permission.setDenied(false);
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isAnyPermissionAvailableToAskAgain(Activity activity) {
        for (BasePermission basePermission : mPermissions) {
            if (!basePermission.shouldNeverAskAgain(activity))
                return true;
        }
        return false;
    }

    public static class Builder {
        private ArrayList<BasePermission> permissions;
        private boolean fatal = false;
        private String title;

        public Builder() {
            permissions = new ArrayList<>();
            title = "";
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder addPermission(BasePermission permission) {
            permissions.add(permission);
            return this;
        }

        public PermissionGroup build() {
            return new PermissionGroup(title, permissions, fatal);
        }
    }
}
