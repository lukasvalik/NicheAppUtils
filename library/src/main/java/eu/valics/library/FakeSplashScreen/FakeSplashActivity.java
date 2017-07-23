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
    private ImageView mLogoImage;
    private ProgressWheel mProgressWheel;
    private TextView mLoadingTextView;

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

        initViews();
        mBackgroundType = getBackgroundType();

        switch (mBackgroundType) {
            case COLOR:
                mRootView.setBackgroundColor(getBackgroundColor());
                break;
            case DRAWABLE:
                mRootView.setBackground(getBackgroundDrawable());
                break;
            default:
                throw new IllegalStateException("Incorrect BackgroundState");
        }
        mLogoImage.setImageDrawable(getLogoDrawable());
        mProgressWheel.setBarColor(getProgressColor());
        mLoadingTextView.setTextColor(getLoadingTextColor());
    }

    private void initViews() {
        mRootView = (RelativeLayout) mContentView.findViewById(R.id.rootView);
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

    protected abstract int getBackgroundColor();
    protected abstract Drawable getBackgroundDrawable();
    protected abstract Drawable getLogoDrawable();
    protected abstract int getProgressColor();
    protected abstract int getLoadingTextColor();

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
        onFinished();
    }

    @Override
    public void onShowedAd() {

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
        if (firstRun && handleFirstRun()) mAppInfo.wasFirstRun();
        mAppInfo.setGoInBackground(true);
        startActivity(getParentActivityIntent());
    }

    /**
     * We may want to be able determine first run in next activity e.g. show tutorial
     * @return whether we handle first run in splash activity
     */

    protected boolean handleFirstRun() {
        return true;
    }
}
