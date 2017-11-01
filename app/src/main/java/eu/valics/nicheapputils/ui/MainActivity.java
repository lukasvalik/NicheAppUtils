package eu.valics.nicheapputils.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import eu.valics.library.Base.AppInfo;
import eu.valics.library.Base.BaseActivity;
import eu.valics.library.Base.BaseApplication;
import eu.valics.library.Utils.permissionmanagement.Permission.CameraPermission;
import eu.valics.library.Utils.permissionmanagement.Permission.NotificationAccessPermission;
import eu.valics.library.Utils.permissionmanagement.Permission.ReadPhoneStatePermission;
import eu.valics.library.Utils.permissionmanagement.PermissionGroup;
import eu.valics.library.Utils.permissionmanagement.PermissionManager;
import eu.valics.nicheapputils.R;

public class MainActivity extends BaseActivity {

    private static final String INCOMING_EVENTS_GROUP_TITLE = "INCOMING_EVENTS_GROUP_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getRootViewId() {
        return R.id.rootView;
    }

    @Override
    protected PermissionManager initActivityPermissionManager() {
        AppInfo appInfo = BaseApplication.getInstance().getAppInfo();

        CameraPermission cameraPermission = new CameraPermission(appInfo);
        cameraPermission.setFatal(true, getString(R.string.camera_permission), getString(R.string.camera_permission_permission_description));

        ReadPhoneStatePermission readPhoneStatePermission = new ReadPhoneStatePermission(appInfo);

        NotificationAccessPermission notificationAccessPermission = new NotificationAccessPermission(appInfo);
/*
                new NotificationAccessPermission(
                appInfo,
                ContextCompat.getDrawable(this, R.drawable.permission_image),
                ContextCompat.getDrawable(this, R.drawable.rounded_button));
*/

        PermissionGroup permissionGroup =
                new PermissionGroup.Builder()
                        .title(INCOMING_EVENTS_GROUP_TITLE)
                        .addPermission(readPhoneStatePermission)
                        //.addPermission(notificationAccessPermission)
                        .build();
        permissionGroup.setFatal(true, getString(R.string.permission_group_title), getString(R.string.permission_group_description));

        return new PermissionManager.Builder(PermissionManager.ACTIVITY_PERMISSION_MANAGER_KEY, mPermissionManagersWrapper)
                .with(this, appInfo)
                .dialogStyle(R.style.AppCompatAlertDialogStyle)
                .addPermission(cameraPermission)
                .addPermissionGroup(permissionGroup)
                .build();
    }


}
