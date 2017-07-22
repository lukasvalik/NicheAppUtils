package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import io.reactivex.Observable;

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

    public boolean enabledAtLeastOnePermission(Context context) {
        return Observable.fromIterable(mPermissions).filter(p -> p.isEnabled(context)).count().blockingGet() > 0;
    }

    public boolean isDenied() {
        return mDenied;
    }

    public void setDenied(boolean denied) {
        mDenied = denied;
    }

    public boolean allOtherPermissionsDenied(BasePermission permission) {
        return Observable.fromIterable(mPermissions).filter(p -> p.getRequestCode() != permission.getRequestCode() && !p.isDenied()).count().blockingGet() == 0;
    }

    public void resetAllDeniedStatuses() {
        mDenied = false;
        Observable.fromIterable(mPermissions).doOnNext(p -> p.setDenied(false));
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isAnyPermissionAvailableToAskAgain(Activity activity) {
        return Observable.fromIterable(mPermissions).filter(p -> !p.shouldNeverAskAgain(activity)).count().blockingGet() > 0;
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
            if (permissions == null) throw new IllegalArgumentException("Permission list cannot be null");
            return new PermissionGroup(title, permissions, fatal);
        }
    }
}
