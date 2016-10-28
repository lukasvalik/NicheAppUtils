package eu.valics.library.Presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import eu.valics.library.AppInfo;
import eu.valics.library.SplashActivityLauncher;
import eu.valics.library.Utils.Ads.BackgroundChecker;
import eu.valics.library.Utils.Ads.InterstitialAdCreator;

/**
 * Created by L on 9/7/2016.
 */
abstract class BackgroundAdHandlingPresenter implements InterstitialAdCreator.InterstitialListener {

    private static final String TAG = "BG_AD_PRESENTER";
    protected Context mContext;
    protected AppInfo mAppInfo;
    protected int mAdFrequency = 5;

    public BackgroundAdHandlingPresenter(Context context) {
        mContext = context;
        mAppInfo = AppInfo.get(context);
    }

    public BackgroundAdHandlingPresenter(Context context, AppInfo appInfo){
        mContext = context;
        mAppInfo = appInfo;
    }

    public void onResume() {
        handleBackgroundStateAndInterstitialAd();
        stopTimer();
    }

    public void onPause() {
        startTimer();
        if (!mAppInfo.isShowingInterstitialAd())
            InterstitialAdCreator.get(mContext).removeListener();
    }

    protected void handleBackgroundStateAndInterstitialAd() {
        if (mAppInfo.wasInBackground() && !mAppInfo.isShowingInterstitialAd()) {
            Log.d(TAG, "was in bg, not showing interstitial");

            int adBuffer = mAppInfo.getBufferForInterstitialAd() + 1;
            mAppInfo.setBufferForInterstitialAd(adBuffer);

            if (adBuffer >= mAdFrequency) {

                if (InterstitialAdCreator.get(mContext).getInterstitialAd().isLoaded()) {
                    InterstitialAdCreator.get(mContext).setListener(this);
                    mAppInfo.setShowingInterstitialAd(true);
                    InterstitialAdCreator.get(mContext).showInterstatialAd();
                    Log.d(TAG, "ad loaded");
                } else if (mAppInfo.isOnline() && !mAppInfo.wasLoadingOfAppIsDone()) {

                    Log.d(TAG, "online and loading of app not done");

                    try {
                        SplashActivityLauncher.launch(mContext); // launcher activity should be splash
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (!mAppInfo.isOnline())
                    Log.d(TAG, "is not online");
                else if (mAppInfo.wasLoadingOfAppIsDone())
                    Log.d(TAG, "was loading of app is done");
            }
        } else {
            mAppInfo.setShowingInterstitialAd(false);
            Log.d(TAG, "was not in bg or showing interstitial");
        }
        if (!mAppInfo.isShowingInterstitialAd()) {
            mAppInfo.setLoadingOfAdIsDone(false);
            Log.d(TAG, "setting loading ad done to false");
        }
    }

    protected void setAdFrequency(int adFrequency){
        mAdFrequency = adFrequency;
    }

    private void startTimer() {
        BackgroundChecker.get(mContext).startBackgroundCheckerTimer();
    }

    private void stopTimer() {
        BackgroundChecker.get(mContext).stopBackgroundCheckerTimer();
    }

    @Override
    public void onLoadedAd() {
        InterstitialAdCreator.get(mContext).showInterstatialAd();
    }

    @Override
    public void onShowedAd() {

        int bulgarianConstant = 1; // firstly it was 2, but now I reduced it to 1

        mAppInfo.setBufferForInterstitialAd(
                mAppInfo.getBufferForInterstitialAd() - mAdFrequency - bulgarianConstant);
    }

    @Override
    public void onClosedAd() {
        mAppInfo.setShowingInterstitialAd(false);
        InterstitialAdCreator.get(mContext).removeListener();
        InterstitialAdCreator.get(mContext).requestNewInterstitial();
    }

    @Override
    public void onNotLoaded() {

    }
}
