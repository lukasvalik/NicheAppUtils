package eu.valics.library.Presenter;

import android.content.Context;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Base.BaseApplication;
import eu.valics.library.SplashActivityLauncher;
import eu.valics.library.Utils.Ads.InterstitialAdCreator;

/**
 * Created by L on 9/7/2016.
 */
abstract class BackgroundAdHandlingPresenter implements InterstitialAdCreator.InterstitialListener {

    protected Context mContext;
    protected AppInfo mAppInfo;
    protected int mAdFrequency;

    private InterstitialAdCreator mInterstitialAdCreator;

    private boolean showingInterstitialAd = false;
    private boolean showedInterstitialAd = false;

    public BackgroundAdHandlingPresenter() {
        BaseApplication application = BaseApplication.getInstance();
        mContext = application.getApplicationContext();
        mAppInfo = application.getAppInfo();
        mAdFrequency = mAppInfo.getAdFrequency();
        mInterstitialAdCreator = application.getInterstitialAdCreator();
    }

    public void onResume() {
        handleBackgroundStateAndInterstitialAd();
        stopTimer();
    }

    public void onPause() {
        startTimer();
        if (!showingInterstitialAd) mInterstitialAdCreator.removeListener();
    }

    protected void handleBackgroundStateAndInterstitialAd() {
        if (mAppInfo.wasInBackground() && !showingInterstitialAd) {

            int adBuffer = mAppInfo.getBufferForInterstitialAd() + 1;
            mAppInfo.setBufferForInterstitialAd(adBuffer);

            if (adBuffer >= mAdFrequency) {

                if (mInterstitialAdCreator.getInterstitialAd().isLoaded()) {
                    mInterstitialAdCreator.setListener(this);
                    showingInterstitialAd = true;
                    mInterstitialAdCreator.showInterstatialAd();
                } else if (mAppInfo.isOnline() && !mAppInfo.wasLoadingOfAppIsDone()) {
                    int decreaseAdBuffer = mAppInfo.getBufferForInterstitialAd() - 1;
                    mAppInfo.setBufferForInterstitialAd(decreaseAdBuffer);
                    SplashActivityLauncher.launch(mContext); // launcher activity should be splash
                }
            }
        } else {
            showingInterstitialAd = false;
        }
        if (!showingInterstitialAd) {
            mAppInfo.setLoadingOfAdIsDone(false);
        }
    }

    public boolean isShowingInterstitialAd() {
        return showingInterstitialAd;
    }

    public boolean isShowedInterstitialAd() {
        return showedInterstitialAd;
    }

    public void setShowedInterstitialAd(boolean showedInterstitialAd) {
        this.showedInterstitialAd = showedInterstitialAd;
    }

    public void setAdFrequency(int adFrequency) {
        mAdFrequency = adFrequency;
    }

    protected int getAdFrequency(){
        return mAdFrequency;
    }

    protected void startTimer() {
        BaseApplication.getBackgroundChecker().startBackgroundCheckerTimer();
    }

    public void stopTimer() {
        BaseApplication.getBackgroundChecker().stopBackgroundCheckerTimer();
    }

    @Override
    public void onLoadedAd() {
        mInterstitialAdCreator.showInterstatialAd();
    }

    @Override
    public void onShowedAd() {
        mAppInfo.setBufferForInterstitialAd(mAppInfo.getBufferForInterstitialAd() - mAdFrequency - InterstitialAdCreator.BULGARIAN_CONSTANT);
    }

    @Override
    public void onClosedAd() {
        showingInterstitialAd = false;
        showedInterstitialAd = true;
        mInterstitialAdCreator.removeListener();
        mInterstitialAdCreator.requestNewInterstitial();
    }

    @Override
    public void onNotLoaded() {

    }
}
