package eu.valics.library.Base;

import android.app.Application;

import eu.valics.library.NicheAppUtils;
import eu.valics.library.R;
import eu.valics.library.Utils.Ads.BackgroundChecker;
import eu.valics.library.Utils.Ads.InterstitialAdCreator;

/**
 * Created by L on 7/22/2017.
 */

public abstract class BaseApplication extends Application {

    private static BackgroundChecker sBackgroundChecker;
    private InterstitialAdCreator mInterstitialAdCreator;

    protected static BaseApplication sInstance;
    protected AppInfo mAppInfo;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sBackgroundChecker = new BackgroundChecker();

        mAppInfo = initAppInfo();
        mAppInfo.setAdFrequency(getAdFrequency());

        mInterstitialAdCreator = new InterstitialAdCreator(getApplicationContext());

        NicheAppUtils.initAds(getInterstitialAdId(), getBannerAdId());
    }

    protected String getBannerAdId(){
        return getString(R.string.banner_ad_id_test);
    }

    protected String getInterstitialAdId() {
        return getString(R.string.interstitial_ad_id_test);
    }

    protected abstract AppInfo initAppInfo();

    protected abstract int getAdFrequency();

    public static BaseApplication getInstance() {
        return sInstance;
    }

    public AppInfo getAppInfo() {
        return mAppInfo;
    }

    public InterstitialAdCreator getInterstitialAdCreator() {
        return mInterstitialAdCreator;
    }

    public static BackgroundChecker getBackgroundChecker() {
        return sBackgroundChecker;
    }
}