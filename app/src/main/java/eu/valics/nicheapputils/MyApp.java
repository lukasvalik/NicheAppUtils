package eu.valics.nicheapputils;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Base.BaseApplication;
import eu.valics.library.NicheAppUtils;

/**
 * Created by L on 9/7/2016.
 */
public class MyApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        NicheAppUtils.initFloatingIcon(
                R.mipmap.ic_launcher,
                getResources().getString(R.string.floating_title),
                getResources().getString(R.string.floating_description),
                getResources().getString(R.string.restartPhrase));
    }

    @Override
    protected AppInfo initAppInfo() {
        return new AppInfo(this);
    }

    @Override
    protected int getAdFrequency() {
        return 1;
    }
}
