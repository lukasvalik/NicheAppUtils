package eu.valics.library.Utils.permissionmanagement;

import android.view.View;

import eu.valics.library.Base.BaseActivity;

/**
 * Created by L on 8/24/2017.
 */

public abstract class OnPermissionClickListener implements View.OnClickListener {

    private BaseActivity mBaseActivity;
    private PermissionManager mPermissionManager;
    private PermissionInvalidationListener mPermissionInvalidationListener;

    OnPermissionClickListener(BaseActivity baseActivity, PermissionManager permissionManager) {
        mBaseActivity = baseActivity;
        mPermissionManager = permissionManager;

        mPermissionInvalidationListener = () -> {
            mBaseActivity.resumeBgTriggeringInterstitialAd();
            if (mPermissionManager.enabledAllFatalPermissions())
                onPermissionGrantedClickListener();
            else
                onPermissionNotGrantedClickListener();
        };
    }

    public abstract void onPermissionGrantedClickListener();

    public abstract void onPermissionNotGrantedClickListener();

    private void onPermissionGrantedClickListenerInternal() {
        mBaseActivity.pauseBgTriggeringInterstitialAd();
        mPermissionManager.setListener(mPermissionInvalidationListener);
        mPermissionManager.invalidatePermissions(true);
    }

    @Override
    public void onClick(View v) {
        onPermissionGrantedClickListenerInternal();
    }
}
