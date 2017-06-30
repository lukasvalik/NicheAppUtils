package eu.valics.nicheapputils.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import eu.valics.library.FakeSplashScreen.FakeSplashActivity;
import eu.valics.nicheapputils.R;

/**
 * Created by L on 9/7/2016.
 */
public class SplashActivity extends FakeSplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressWheelColor = ContextCompat.getColor(this, R.color.colorPrimary);
        mLoadingTextColor = ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    protected FakeSplashActivity.BackgroundType getBackgroundType() {
        return BackgroundType.COLOR;
    }
}
