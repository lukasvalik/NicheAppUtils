package eu.valics.nicheapputils.ui;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import eu.valics.library.FakeSplashScreen.FakeSplashActivity;
import eu.valics.nicheapputils.R;

/**
 * Created by L on 9/7/2016.
 */
public class SplashActivity extends FakeSplashActivity {

    @Override
    protected int getBackgroundColor() {
        return 0;
    }

    @Override
    protected Drawable getBackgroundDrawable() {
        return ContextCompat.getDrawable(this, R.drawable.loading_screen);
    }

    @Override
    protected Drawable getLogoDrawable() {
        return null;
    }

    @Override
    protected int getProgressColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    protected int getLoadingTextColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    protected FakeSplashActivity.BackgroundType getBackgroundType() {
        return BackgroundType.DRAWABLE;
    }
}
