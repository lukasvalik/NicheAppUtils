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
        mInterstitialAdCreator = BaseApplication.getInterstitialAdCreator();
    }

    public void onResume() {
        handleBackgroundStateAndInterstitialAd();
        stopTimer();
    }

    public void onPause(boolean pauseBgTriggeringInterstitialAd) {
        if (!pauseBgTriggeringInterstitialAd) startTimer();
        if (!showingInterstitialAd) mInterstitialAdCreator.removeListener();
    }

    public boolean isGoingToLoadInterstitialAd() {
        boolean wasInBg = mAppInfo.wasInBackground();
        boolean showingAd = showingInterstitialAd;
        int buffer = mAppInfo.getBufferForInterstitialAd();
        boolean bufferOverflow = buffer + 1 >= mAdFrequency;
        boolean adLoaded = mInterstitialAdCreator.getInterstitialAd().isLoaded();
        //boolean splashLoadedDone = mAppInfo.isOnline() && !mAppInfo.wasLoadingOfAppIsDone();
        return wasInBg && ! showingAd && bufferOverflow && !adLoaded;
    }

    public boolean isGoingToPauseActivity() {
        boolean wasInBg = mAppInfo.wasInBackground();
        boolean showingAd = showingInterstitialAd;
        int buffer = mAppInfo.getBufferForInterstitialAd();
        boolean bufferOverflow = buffer + 1 >= mAdFrequency;
        boolean adLoaded = mInterstitialAdCreator.getInterstitialAd().isLoaded();
        //boolean splashLoadedDone = mAppInfo.isOnline() && !mAppInfo.wasLoadingOfAppIsDone();
        return wasInBg && ! showingAd && bufferOverflow && adLoaded;
    }

    protected void handleBackgroundStateAndInterstitialAd() {
        if (mAppInfo.wasInBackground() && !showingInterstitialAd) {

            int adBuffer = mAppInfo.getBufferForInterstitialAd() + 1;

            if (adBuffer >= mAdFrequency) {
                if (mInterstitialAdCreator.getInterstitialAd().isLoaded()) {
                    mInterstitialAdCreator.setListener(this);
                    showingInterstitialAd = true;
                    mInterstitialAdCreator.showInterstatialAd();
                } else if (mAppInfo.isOnline() && !mAppInfo.wasLoadingOfAppIsDone()) {
                    //int decreaseAdBuffer = mAppInfo.getBufferForInterstitialAd() - 1;
                    //mAppInfo.setBufferForInterstitialAd(decreaseAdBuffer);
                    SplashActivityLauncher.launch(mContext); // launcher activity should be splash
                } else {
                    mAppInfo.setBufferForInterstitialAd(adBuffer);
                }
            } else {
                mAppInfo.setBufferForInterstitialAd(adBuffer);
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
        mAppInfo.setBufferForInterstitialAd(mAppInfo.getBufferForInterstitialAd() - mAdFrequency);
    }

    @Override
    public void onClosedAd() {
        showingInterstitialAd = false;
        showedInterstitialAd = true;
        mInterstitialAdCreator.removeListener();
        mInterstitialAdCreator.resetInterstitialAd(mContext); // always need to be showed loading, this is easiest way to
    }

    @Override
    public void onNotLoaded() {

    }
}
