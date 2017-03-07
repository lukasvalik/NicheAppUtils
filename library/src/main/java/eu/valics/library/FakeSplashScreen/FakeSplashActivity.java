package eu.valics.library.FakeSplashScreen;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import eu.valics.library.AppInfo;
import eu.valics.library.R;
import eu.valics.library.Utils.Ads.BackgroundChecker;
import eu.valics.library.Utils.Ads.FakeLoading;
import eu.valics.library.Utils.Ads.InterstitialAdCreator;
import eu.valics.library.Utils.Ads.OnFinishedListener;

/**
 * Created by L on 9/7/2016.
 */
public class FakeSplashActivity extends AppCompatActivity implements OnFinishedListener,
        InterstitialAdCreator.InterstitialListener{

    private View mContentView;
    private FakeLoading mFakeLoading;
    private RelativeLayout mRootView;
    private ImageView mLogoImage;
    private ProgressWheel mProgressWheel;
    private TextView mLoadingTextView;

    protected int mBackgroundColor;
    protected Drawable mLogoDrawable;
    protected int mProgressWheelColor;
    protected int mLoadingTextColor;

    private int mAdFrequency = 5;
    private AppInfo mAppInfo;

    private boolean waitTillAdWillClose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.NoActionBarActivity);

        mContentView = new FakeSplashContentView(this);
        setContentView(mContentView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setBackgroundColor(mBackgroundColor);
        setLogo(mLogoDrawable);
        setProgressColor(mProgressWheelColor);
        setLoadingTextColor(mLoadingTextColor);
    }

    @Override
    public void onResume(){
        super.onResume();
        interstitialAdLogic();
    }

    private void interstitialAdLogic(){

        mFakeLoading = new FakeLoading();
        mAppInfo = AppInfo.get(this);

        if (mAppInfo.isOnline()) {
            if (mAppInfo.isFirstRun()) {
                mAppInfo.wasFirstRun();
                mAppInfo.setBufferForInterstitialAd(mAdFrequency);
                InterstitialAdCreator.get(this).requestNewInterstitial();
                InterstitialAdCreator.get(this).setListener(this);
                mFakeLoading.startLoading(this);
            } else if (mAppInfo.getBufferForInterstitialAd() >= mAdFrequency-1) {
                InterstitialAdCreator.get(this).requestNewInterstitial();
                InterstitialAdCreator.get(this).setListener(this);
                mFakeLoading.startLoading(this);
            } else {
                mFakeLoading.startHalfSecLoading(this);
            }
        } else {
            mFakeLoading.startHalfSecLoading(this);
        }

        mAppInfo.setGoInBackground(false);
    }

    @Override
    public void onPause(){
        mFakeLoading.unregister();
        //InterstitialAdCreator.get(this).removeListener();
        mAppInfo.setLoadingOfAdIsDone(true);
        super.onPause();
        //if (!waitTillAdWillClose) {
            BackgroundChecker.get(this).startBackgroundCheckerTimer();
            finish();
        //}
    }

    /**
     * UI Properties
     */

    protected void setBackgroundColor(int color){
        mRootView = (RelativeLayout) mContentView.findViewById(R.id.rootView);
        mRootView.setBackgroundColor(color);
    }

    protected void setLogo(Drawable logoDrawable){
        mLogoImage = (ImageView) mContentView.findViewById(R.id.iconLogo);
        mLogoImage.setImageDrawable(logoDrawable);
    }

    protected void setProgressColor(int color){
        mProgressWheel = (ProgressWheel) mContentView.findViewById(R.id.progress_wheel);
        mProgressWheel.setBarColor(color);
    }

    protected void setLoadingTextColor(int color){
        mLoadingTextView = (TextView) mContentView.findViewById(R.id.loadingText);
        mLoadingTextView.setTextColor(color);
    }

    protected void setFullscreen(){
        setTheme(R.style.NoActionBarFullscreenActivity);
    }

    /**
     *
     * frequency number of opening app or returning to app from background without showing AD
     */

    protected void setAdFrequency(int frequency){
        mAdFrequency = frequency;
    }

    /**
     * Interstitial Ad callbacks
     */

    @Override
    public void onLoadedAd() {
        //waitTillAdWillClose = true;
        InterstitialAdCreator.get(this).showInterstatialAd();
    }

    @Override
    public void onShowedAd() {
        int bulgarianConstant = 2; // firstly it was 2, but now I reduced it to 1

        mAppInfo.setBufferForInterstitialAd(
                mAppInfo.getBufferForInterstitialAd() - mAdFrequency - bulgarianConstant);

        onFinished();
    }

    @Override
    public void onClosedAd() {
        //waitTillAdWillClose = false;
        //onFinished();
    }

    @Override
    public void onNotLoaded() {

    }

    /**
     * OnFinished fake loading, or loading add.
     * Set flag in background to let next activity know that it should control whether show
     * interstitial ad, or what...
     * Move to next Activity
     */

    @Override
    public void onFinished() {
        InterstitialAdCreator.get(this).removeListener();
        mAppInfo.setGoInBackground(true);
        startActivity(getParentActivityIntent());
    }
}
