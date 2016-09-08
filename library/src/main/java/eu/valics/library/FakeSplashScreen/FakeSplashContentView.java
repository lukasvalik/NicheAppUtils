package eu.valics.library.FakeSplashScreen;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import eu.valics.library.R;

/**
 * Created by L on 9/7/2016.
 */
public class FakeSplashContentView extends RelativeLayout {

    public FakeSplashContentView(Context context) {
        super(context);
        initialize(context);
    }

    public FakeSplashContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FakeSplashContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
}

    private void initialize(Context context){
        inflate(context, R.layout.activity_splash_fake, this);
    }
}
