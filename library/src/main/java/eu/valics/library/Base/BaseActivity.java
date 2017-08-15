package eu.valics.library.Base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import eu.valics.library.Presenter.AdPresenter;
import eu.valics.library.Utils.permissionmanagement.PermissionInvalidationListener;
import eu.valics.library.Utils.permissionmanagement.PermissionManager;

/**
 * Created by L on 7/19/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements PermissionInvalidationListener {

    protected AdPresenter mAdPresenter;
    protected PermissionManager mActivityPermissionManager;
    protected RelativeLayout mRootView;

    protected boolean interstialAdHandled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdPresenter = initAdPresenter();
        mActivityPermissionManager = initActivityPermissionManager();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mRootView = getRootViewId() != -1 ? (RelativeLayout) findViewById(getRootViewId()) : null;
    }

    protected AdPresenter initAdPresenter() {
        return new AdPresenter();
    }

    protected PermissionManager initActivityPermissionManager() {
        return new PermissionManager.Builder().with(BaseActivity.this, BaseApplication.getInstance().getAppInfo()).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivityPermissionManager.setListener(this);
        invalidateAppPausingProcesses();
    }

    public void invalidateAppPausingProcesses() {
        if (!interstialAdHandled) {
            interstialAdHandled = true;
            boolean isGonnaPause = mAdPresenter.isGoingToPauseActivity();
            mAdPresenter.onResume();
            if (!isGonnaPause) onInterstitialAdHandled();
        } else {
            mAdPresenter.stopTimer();
            onInterstitialAdHandled();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdPresenter.onPause();
        mActivityPermissionManager.setListener(null);
    }

    protected void onInterstitialAdHandled() {
        mActivityPermissionManager.invalidatePermissions(false);
        if (mActivityPermissionManager.getPermissionInProgress() == null) {
            onActivityPermissionsHandled();
        }
    }

    protected void onActivityPermissionsHandled() {
        mAdPresenter.setShowedInterstitialAd(false);
        interstialAdHandled = false;
        if (mRootView != null) //
            mAdPresenter.showBanner(this, (RelativeLayout) findViewById(getRootViewId()));
        onReadyResume();
    }

    protected void onReadyResume(){}

    /**
     * Override when want automatically have banner Ad on bottom of screen
     *
     * @return id of root relativeLayout
     */
    protected int getRootViewId() {
        return -1;
    }

    protected AdPresenter getAdPresenter() {
        return mAdPresenter;
    }

    protected PermissionManager getActivityPermissionManager() {
        return mActivityPermissionManager;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Now user should be able to use feature
            mActivityPermissionManager.onPermissionGranted(requestCode);
        } else {
            // Your app will not have this permission. Turn off all functions
            // that require this permission or it will force close
            mActivityPermissionManager.onPermissionNotGranted(requestCode);
        }
    }

    @Override
    public void onPermissionInvalidated() {
        //TODO here I could turn off activity probably
        interstialAdHandled = false;
    }
}
