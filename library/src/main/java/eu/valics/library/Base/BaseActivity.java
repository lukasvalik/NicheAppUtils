package eu.valics.library.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import eu.valics.library.Presenter.AdPresenter;
import eu.valics.library.Utils.permissionmanagement.PermissionManager;

/**
 * Created by L on 7/19/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected AdPresenter mAdPresenter;
    protected PermissionManager mActivityPermissionManager;

    protected boolean interstialAdHandled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdPresenter = initAdPresenter();
        mActivityPermissionManager = initActivityPermissionManager();
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
        if (!interstialAdHandled)
            mAdPresenter.onResume();
        else {
            mAdPresenter.stopTimer();
            onInterstitialAdHandled();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdPresenter.onPause();
    }

    protected void onInterstitialAdHandled() {
        interstialAdHandled = true;
        /*
        if (shouldCheckActivityPermissions()) {

        }
        */
        onActivityPermissionsHandled();
    }

    protected void onActivityPermissionsHandled() {
        onReadyResume();
    }

    protected abstract void onReadyResume();

    protected AdPresenter getAdPresenter() {
        return mAdPresenter;
    }

    protected PermissionManager getActivityPermissionManager() {
        return mActivityPermissionManager;
    }
}
