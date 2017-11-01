package eu.valics.library.Utils.Ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import eu.valics.library.NicheAppUtils;

/**
 * Created by L on 9/7/2016.
 */
public class InterstitialAdCreator {

    private static final String DEV_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712";

    /**
     * In contructor of Presenter create new Builder.
     * In OnResume requestNewInterstatial
     * showInterstatial handles not loaded as well and skips it
     */

    // not implemented change yet
    public enum state {
        NOT_LOADING, IS_LOADING, LOADED
    }

    public interface InterstitialListener {
        void onLoadedAd();

        void onShowedAd();

        void onClosedAd();

        void onNotLoaded();
    }

    private InterstitialAd mInterstitialAd;

    private InterstitialListener mListener;

    private state mState;

    public InterstitialAdCreator(Context context) {
        resetInterstitialAd(context);
    }

    public void resetInterstitialAd(Context context) {
        mInterstitialAd = new InterstitialAd(context.getApplicationContext());
        mInterstitialAd.setAdUnitId(NicheAppUtils.getInterstitialAdId() == null ?
                DEV_INTERSTITIAL_AD_ID :
                NicheAppUtils.getInterstitialAdId());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mState = state.LOADED;
                if (mListener != null)
                    mListener.onLoadedAd();
            }

            @Override
            public void onAdClosed() {
                mState = state.NOT_LOADING;
                if (mListener != null)
                    mListener.onClosedAd();
            }
        });
    }

    public void setAdUnitId(String adUnitId) {
        mInterstitialAd.setAdUnitId(adUnitId);
    }

    /**
     * Can be updated to have buffer of interstatial, which will be requested to get one
     */

    public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        try {
            mState = state.IS_LOADING;
            mInterstitialAd.loadAd(adRequest);
        } catch (Exception e) {
            //Log.e("Unable to load ad", e.getMessage());
            mState = state.NOT_LOADING;
        }

    }

    public void showInterstatialAd() {
        if (mInterstitialAd.isLoaded()) {
            if (mListener != null)
                mListener.onShowedAd();
            mInterstitialAd.show();
        } else {
            if (mListener != null)
                mListener.onNotLoaded();
        }
    }

    public InterstitialAd getInterstitialAd() {
        return mInterstitialAd;
    }

    public InterstitialListener getListener() {
        return mListener;
    }

    public void setListener(InterstitialListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        mListener = null;
    }

    public state getState() {
        return mState;
    }

    public void setState(state state) {
        mState = state;
    }
}
