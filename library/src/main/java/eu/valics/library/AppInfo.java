package eu.valics.library;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by L on 9/7/2016.
 */
public class AppInfo {

    protected static final int BUFFER_DEFAULT = -1;

    protected static AppInfo sAppInfo;
    protected Context mContext;

    protected String APP_PREFERENCIES;
    protected String FIRST_RUN_CHECK;
    protected String BUFFER_FOR_INTERSTATIAL_AD;
    public String WAS_IN_BACKGROUND;

    // this prevents infinite loop of loading screens when there is problem with internet
    // phone thinks is connect, however it is not
    protected boolean loadingOfAdIsDone = false;

    // Prevents before loading multiple ads, if app has very full adBuffer and is opening ad by ad
    protected boolean showingInterstitialAd = false;

    protected boolean firstSplashBeforeMainActivity;

    protected AppInfo(Context context){
        mContext = context;

        firstSplashBeforeMainActivity = true;

        APP_PREFERENCIES = mContext.getPackageName();
        FIRST_RUN_CHECK = APP_PREFERENCIES + ".isFirstRun";
        BUFFER_FOR_INTERSTATIAL_AD = APP_PREFERENCIES + ".bufferForInterstitialAd";
        WAS_IN_BACKGROUND = APP_PREFERENCIES + ".was_in_background";
    }

    public static AppInfo get(Context context){
        if (sAppInfo == null)
            sAppInfo = new AppInfo(context.getApplicationContext());
        return sAppInfo;
    }

    public boolean isOnline(){
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isFirstRun(){
        return mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE)
                .getBoolean(FIRST_RUN_CHECK, true);
    }

    public void wasFirstRun(){
        mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE).edit()
                .putBoolean(FIRST_RUN_CHECK, false).commit();
    }

    public void setBufferForInterstitialAd(int t){
        mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE).edit()
                .putInt(BUFFER_FOR_INTERSTATIAL_AD, t).commit();
    }

    public int getBufferForInterstitialAd(){
        return mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE)
                .getInt(BUFFER_FOR_INTERSTATIAL_AD, BUFFER_DEFAULT);
    }

    public boolean wasInBackground(){
        return mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE)
                .getBoolean(WAS_IN_BACKGROUND, false);
    }

    public void setGoInBackground(boolean goingToBackground){
        mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE).edit().
                putBoolean(WAS_IN_BACKGROUND, goingToBackground).commit();
    }

    /**
     * Variables related only to life of App, no need to save them
     */

    public boolean wasLoadingOfAppIsDone() {
        return loadingOfAdIsDone;
    }

    public void setLoadingOfAdIsDone(boolean loadingOfAppIsDone) {
        this.loadingOfAdIsDone = loadingOfAppIsDone;
    }

    public boolean isShowingInterstitialAd() {
        return showingInterstitialAd;
    }

    public void setShowingInterstitialAd(boolean alreadyShowedInterstitialAd) {
        this.showingInterstitialAd = alreadyShowedInterstitialAd;
    }

    public boolean isFirstSplashBeforeMainActivity() {
        return firstSplashBeforeMainActivity;
    }

    public void setFirstSplashBeforeMainActivity(boolean first){
        firstSplashBeforeMainActivity = first;
    }

    public void setAskedPermission(int permissionRequestCode) {
        mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE).edit()
                .putBoolean("Permission_" + Integer.toString(permissionRequestCode), true).apply();
    }

    public boolean wasAskedPermission(int permissionRequestCode) {
        return mContext.getSharedPreferences(APP_PREFERENCIES, Context.MODE_PRIVATE)
                .getBoolean("Permission_" + Integer.toString(permissionRequestCode), false);
    }
}
