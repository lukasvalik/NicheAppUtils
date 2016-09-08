package eu.valics.library.Utils.Ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by L on 9/7/2016.
 */
public class InterstitialAdCreator {

    private static final String DEV_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712";

    private static InterstitialAdCreator sInterstitialAdCreator;

    /**
     * In contructor of Presenter create new Builder.
     * In OnResume requestNewInterstatial
     * showInterstatial handles not loaded as well and skips it
     */

    public interface InterstitialListener{
        void onLoadedAd();
        void onShowedAd();
        void onClosedAd();
        void onNotLoaded();
    }

    private InterstitialAd mInterstitialAd;

    private InterstitialListener mListener;

    private InterstitialAdCreator(Context context){

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(DEV_INTERSTITIAL_AD_ID);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(){
                if(mListener !=null)
                    mListener.onLoadedAd();
            }
            @Override
            public void onAdClosed() {
                if(mListener !=null)
                    mListener.onClosedAd();
            }
        });
    }

    public static InterstitialAdCreator get(Context context){
        if (sInterstitialAdCreator == null)
            sInterstitialAdCreator = new InterstitialAdCreator(context.getApplicationContext());
        return sInterstitialAdCreator;
    }



    public void setAdUnitId(String adUnitId){
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
            mInterstitialAd.loadAd(adRequest);
        } catch (Exception e){
            //Log.e("Unable to load ad", e.getMessage());
        }

    }

    public void showInterstatialAd(){
        if (mInterstitialAd.isLoaded()) {
            if(mListener!=null)
                mListener.onShowedAd();
            mInterstitialAd.show();
        } else {
            if(mListener!=null)
                mListener.onNotLoaded();
        }
    }

    public InterstitialAd getInterstitialAd(){
        return mInterstitialAd;
    }

    public InterstitialListener getListener() {
        return mListener;
    }

    public void setListener(InterstitialListener listener) {
        mListener = listener;
    }

    public void removeListener(){
        mListener = null;
    }
}
