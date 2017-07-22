package eu.valics.nicheapputils.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;

import eu.valics.library.Base.BaseActivity;
import eu.valics.nicheapputils.R;

public class MainActivity extends BaseActivity {

    private static final String INCOMING_EVENTS_GROUP_TITLE = "INCOMING_EVENTS_GROUP_TITLE";

    private RelativeLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = (RelativeLayout) findViewById(R.id.rootView);
        mAdPresenter.setAdFrequency(2);
    }

    @Override
    protected void onReadyResume() {
        mAdPresenter.showBanner(this, mRootView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdPresenter.hideBanner();
    }

    /*
        @Override
        protected PermissionManager getActivityPermissionManager() {

            CameraPermission cameraPermission = new CameraPermission(AppInfo.get(this));
            cameraPermission.setFatal(true, getString(R.string.camera_permission), getString(R.string.camera_permission_permission_description));
            ReadPhoneStatePermission readPhoneStatePermission = new ReadPhoneStatePermission(AppInfo.get(this));
            NotificationAccessPermission notificationAccessPermission = new NotificationAccessPermission(AppInfo.get(this));
            PermissionGroup permissionGroup =
                    new PermissionGroup.Builder()
                            .title(INCOMING_EVENTS_GROUP_TITLE)
                            .addPermission(readPhoneStatePermission)
                            .addPermission(notificationAccessPermission)
                            .build();
            permissionGroup.setFatal(true, getString(R.string.permission_group_title), getString(R.string.permission_group_description));

            return new PermissionManager.Builder()
                    .with(this, AppInfo.get(this))
                    .addPermission(cameraPermission)
                    .addPermissionGroup(permissionGroup)
                    .build();
        }
    */


}
