package eu.valics.nicheapputils.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import eu.valics.nicheapputils.R;

public class MainActivity extends AppCompatActivity {

    private MainPresenter mPresenter;

    private RelativeLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);

        mRootView = (RelativeLayout) findViewById(R.id.rootView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        mPresenter.showBanner(this, mRootView);
    }

    @Override
    protected void onPause() {
        mPresenter.onPause();
        super.onPause();
    }
}
