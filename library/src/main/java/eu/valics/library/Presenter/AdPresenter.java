package eu.valics.library.Presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import eu.valics.library.AppInfo;
import eu.valics.library.NicheAppUtils;

/**
 * Created by L on 9/8/2016.
 */
public class AdPresenter extends BackgroundAdHandlingPresenter {

    private static final String BANNER_AD_DEV_ID = "ca-app-pub-3940256099942544/6300978111";

    private AdView mAdView;
    private boolean mAdAlreadyAddedToView = false;

    public AdPresenter(Context context) {
        super(context);
    }

    public AdPresenter(Context context, AppInfo appInfo){
        super(context, appInfo);
    }

    @Override
    public void onPause(RelativeLayout rootView) {
        if (mAdView != null) {
            mAdView.destroy();
            rootView.removeView(mAdView);
            mAdView = null;
        }
        super.onPause(rootView);
    }

    public void showBanner(final Activity activity,
                           RelativeLayout rootView) {

        if (mAdView == null) {
            mAdView = new AdView(mContext);
            mAdView.setAdSize(AdSize.BANNER);

            if (NicheAppUtils.getBannerId() != null)
                mAdView.setAdUnitId(NicheAppUtils.getBannerId());
            else
                mAdView.setAdUnitId(BANNER_AD_DEV_ID);
        }

        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdView.setBackgroundColor(Color.BLACK);
                        mAdView.setAdListener(null);
                    }
                });
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        if (!mAdAlreadyAddedToView) {
            rootView.addView(mAdView, params);
            mAdAlreadyAddedToView = true;
        }

        mAdView.setVisibility(View.VISIBLE);
        mAdView.setEnabled(true);
    }


    public void hideBanner() {
        mAdView.setVisibility(View.GONE);
        mAdView.setEnabled(false);
    }

    /**
     * This method is meant to be used in case when we show soft keyboard and we need to remove ad
     * to not take space while screen is smaller due to expanded soft keyboard
     *
     * activity    need this because we want to update UI thread
     * rootView    parentView, where we can add AdView
     * adContainer this is dummyView, which knows size of ad and holds place for it at the
     *                    bottom of the screen to prevent overlaying of some views by bannerAd
     */

    public void showRemovedBanner(final Activity activity,
                                  RelativeLayout rootView,
                                  FrameLayout adContainer) {

        if (mAdView == null) {
            mAdView = new AdView(mContext);
            mAdView.setAdSize(AdSize.BANNER);

            if (NicheAppUtils.getBannerId() != null)
                mAdView.setAdUnitId(NicheAppUtils.getBannerId());
            else
                mAdView.setAdUnitId(BANNER_AD_DEV_ID);

        }

        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdView.setBackgroundColor(Color.BLACK);
                        mAdView.setAdListener(null);
                    }
                });
            }
        });


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        rootView.removeView(mAdView);
        rootView.addView(mAdView, params);

        adContainer.setVisibility(View.VISIBLE);
    }

    /**
     * This method is meant to be used in case when we show soft keyboard and we need to remove ad
     * to not take space while screen is smaller due to expanded soft keyboard
     *
     * adContainer this is dummyView, which knows size of ad and holds place for it at the
     *                    bottom of the screen to prevent overlaying of some views by bannerAd
     * rootView parentView, where we can add AdView
     */

    public void removeBanner(RelativeLayout rootView, FrameLayout adContainer) {
        rootView.removeView(mAdView);
        adContainer.setVisibility(View.GONE);
    }
}
