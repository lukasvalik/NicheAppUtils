package eu.valics.nicheapputils;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Base.BaseApplication;
import eu.valics.library.FloatingIcon.FloatingIconConfig;
import eu.valics.library.NicheAppUtils;

/**
 * Created by L on 9/7/2016.
 */
public class MyApp extends BaseApplication {

    @Override
    protected AppInfo initAppInfo() {
        return new AppInfo(this);
    }

    @Override
    protected int getAdFrequency() {
        return 1;
    }

    @Override
    public FloatingIconConfig initFloatingIconConfiguration() {
        return new FloatingIconConfig(
                R.mipmap.ic_launcher,
                getString(R.string.floating_title),
                getString(R.string.floating_description),
                getString(R.string.restartPhrase));
    }
}
