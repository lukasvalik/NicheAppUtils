package eu.valics.nicheapputils;

import android.app.Application;

import eu.valics.library.NicheAppUtils;

/**
 * Created by L on 9/7/2016.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NicheAppUtils.initFloatingIcon(
                R.mipmap.ic_launcher,
                getResources().getString(R.string.floating_title),
                getResources().getString(R.string.floating_description),
                getResources().getString(R.string.restartPhrase));
    }
}
