package eu.valics.library.Utils.permissionmanagement;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by L on 8/27/2017.
 *
 * Wrapper has list of all subsribed permissionManagers to be aware of which PermissionManager is
 * requesting permissions right now
 */

public class PermissionManagersWrapper implements OnPermissionRequestedListener {

    private ArrayList<PermissionManager> mPermissionManagers;
    private PermissionManager mRequestingPermissionManager;

    public PermissionManagersWrapper(){
        mPermissionManagers = new ArrayList<>();
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
        //if (Observable.fromIterable(mPermissionManagers).filter(manager -> manager.getKey().equals(permissionManager.getKey())).count().blockingGet() > 0)
        //    throw new IllegalStateException("Adding multiple permissionManagers with the same key. Already have permissionManager with this key");
        permissionManager.setPermissionRequestedListener(this);
        mPermissionManagers.add(permissionManager);
    }

    public void removePermissionManager(String key) {
        int clearPos = - 1;
        for (int i = 0; i < mPermissionManagers.size(); i++) {
            if (mPermissionManagers.get(i).getKey().equals(key))
                clearPos = i;
        }
        if (clearPos != -1) mPermissionManagers.remove(clearPos);
    }

    public void clearPermissionManagers() {
        mPermissionManagers.clear();
    }

    public void onPermissionGranted(int requestCode) {
        if (mRequestingPermissionManager == null) throw new IllegalStateException("RequestPermission is null");
        mRequestingPermissionManager.onPermissionGranted(requestCode);
    }

    public void onPermissionNotGranted(int requestCode) {
        if (mRequestingPermissionManager == null) throw new IllegalStateException("RequestPermission is null");
        mRequestingPermissionManager.onPermissionNotGranted(requestCode);
    }

    @Override
    public void onPermissionRequested(PermissionManager permissionManager) {
        mRequestingPermissionManager = permissionManager;
    }
}
