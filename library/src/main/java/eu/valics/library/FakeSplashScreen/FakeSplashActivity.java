package eu.valics.library.FakeSplashScreen;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Base.BaseApplication;
import eu.valics.library.R;
import eu.valics.library.Utils.Ads.FakeLoading;
import eu.valics.library.Utils.Ads.InterstitialAdCreator;
import eu.valics.library.Utils.Ads.OnFinishedListener;

/**
 * Created by L on 9/7/2016.
 */
public abstract class FakeSplashActivity extends AppCompatActivity implements OnFinishedListener,
        InterstitialAdCreator.InterstitialListener {

    private View mContentView;
    private FakeLoading mFakeLoading;
    private RelativeLayout mRootView;
    private ImageView mBackgroundImage;
    private ImageView mLogoImage;
    private ProgressWheel mProgressWheel;
    private TextView mLoadingTextView;

    protected int mBackgroundColor;
    protected Drawable mBackgroundDrawable;
    protected Drawable mLogoDrawable;
    protected int mProgressWheelColor;
    protected int mLoadingTextColor;

    private int mAdFrequency;
    private InterstitialAdCreator mInterstitialAdCreator;

    private AppInfo mAppInfo;

    protected enum BackgroundType {COLOR, DRAWABLE}

    protected BackgroundType mBackgroundType = BackgroundType.COLOR;

    boolean firstRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.NoActionBarActivity);

        mContentView = new FakeSplashContentView(this);
        setContentView(mContentView);

        BaseApplication application = BaseApplication.getInstance();
        mAdFrequency = application.getAppInfo().getAdFrequency();
        mInterstitialAdCreator = BaseApplication.getInterstitialAdCreator();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
        mBackgroundType = getBackgroundType();

        switch (mBackgroundType) {
            case COLOR:
                setBackgroundColor(mBackgroundColor);
                break;
            case DRAWABLE:
                setBackgroundDrawable(mBackgroundDrawable);
                break;
            default:
                throw new IllegalStateException("Incorrect BackgroundState");
        }
        setLogo(mLogoDrawable);
        setProgressColor(mProgressWheelColor);
        setLoadingTextColor(mLoadingTextColor);
    }

    private void initViews() {
        mRootView = (RelativeLayout) mContentView.findViewById(R.id.rootView);
        mBackgroundImage = (ImageView) mContentView.findViewById(R.id.backgroundImage);
        mLogoImage = (ImageView) mContentView.findViewById(R.id.iconLogo);
        mProgressWheel = (ProgressWheel) mContentView.findViewById(R.id.progress_wheel);
        mLoadingTextView = (TextView) mContentView.findViewById(R.id.loadingText);
    }

    @Override
    public void onResume() {
        super.onResume();
        interstitialAdLogic();
    }

    private void interstitialAdLogic() {

        mFakeLoading = new FakeLoading();
        mAppInfo = BaseApplication.getInstance().getAppInfo();

        if (mAppInfo.isOnline()) {
            if (mAppInfo.isFirstRun()) {
                firstRun = true;
                mAppInfo.setBufferForInterstitialAd(mAdFrequency);
                mInterstitialAdCreator.requestNewInterstitial();
                mInterstitialAdCreator.setListener(this);
                mFakeLoading.startLoading(this);
            } else if (mAppInfo.getBufferForInterstitialAd() >= mAdFrequency - 1) {
                mInterstitialAdCreator.requestNewInterstitial();
                mInterstitialAdCreator.setListener(this);
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
    public void onPause() {
        super.onPause();
        mFakeLoading.unregister();
        mInterstitialAdCreator.removeListener();
        mAppInfo.setLoadingOfAdIsDone(true);
        BaseApplication.getBackgroundChecker().startBackgroundCheckerTimer();
        finish();
    }

    /**
     * UI Properties
     */

    protected void setBackgroundColor(int color) {
        mRootView.setBackgroundColor(color);
    }

    protected void setBackgroundDrawable(Drawable drawable) {
        mBackgroundImage.setImageDrawable(drawable);
    }

    protected void setLogo(Drawable logoDrawable) {
        mLogoImage.setImageDrawable(logoDrawable);
    }

    protected void setProgressColor(int color) {
        mProgressWheel.setBarColor(color);
    }

    protected void setLoadingTextColor(int color) {
        mLoadingTextView.setTextColor(color);
    }

    protected void setFullscreen() {
        setTheme(R.style.NoActionBarFullscreenActivity);
    }

    /**
     * @return whether background will be drawable or color
     */

    protected abstract BackgroundType getBackgroundType();

    /**
     * Interstitial Ad callbacks
     */

    @Override
    public void onLoadedAd() {
        mInterstitialAdCreator.showInterstatialAd();
    }

    @Override
    public void onShowedAd() {
        if (!firstRun) mAppInfo.setBufferForInterstitialAd(mAppInfo.getBufferForInterstitialAd() - mAdFrequency - InterstitialAdCreator.BULGARIAN_CONSTANT);
        onFinished();
    }

    @Override
    public void onClosedAd() {

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
        mInterstitialAdCreator.removeListener();
        if (firstRun) mAppInfo.wasFirstRun();
        mAppInfo.setGoInBackground(true);
        startActivity(getParentActivityIntent());
    }
}
