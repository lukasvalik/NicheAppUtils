package eu.valics.library.Utils.permissionmanagement;

import java.util.ArrayList;

/**
 * Created by L on 8/27/2017.
 *
 * Wrapper has list of all subsribed permissionManagers and
 */

public class PermissionManagersWrapper implements OnPermissionRequestedListener {

    private ArrayList<PermissionManager> mPermissionManagers;
    private PermissionManager mRequestingPermissionManager;

    public PermissionManagersWrapper(PermissionManager activityPermissionManager){
        mPermissionManagers = new ArrayList<>();
        addPermissionManager(activityPermissionManager);
    }

    public void subscribeAllPermissionManagers() {
        for (PermissionManager permissionManager : mPermissionManagers) {
            permissionManager.setPermissionRequestedListener(this);
        }
    }

    public void unSubscribeAllPermissionManagers() {
        for (PermissionManager permissionManager : mPermissionManagers) {
            permissionManager.setPermissionRequestedListener(null);
        }
    }

    public void addPermissionManager(PermissionManager permissionManager) {
        permissionManager.setPermissionRequestedListener(this);
        mPermissionManagers.add(permissionManager);
    }

    public void onPermissionGranted(int requestCode) {
        mRequestingPermissionManager.onPermissionGranted(requestCode);
    }

    public void onPermissionNotGranted(int requestCode) {
        mRequestingPermissionManager.onPermissionNotGranted(requestCode);
    }

    @Override
    public void onPermissionRequested(PermissionManager permissionManager) {
        mRequestingPermissionManager = permissionManager;
    }
}
