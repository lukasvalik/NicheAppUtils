package eu.valics.library.Utils.FlashManagement;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by L on 7/7/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FlashManagerMarshmallow implements FlashManager {

    private CameraManager mCameraManager;
    private String mCameraId;

    public FlashManagerMarshmallow(Context context){
        initCamera(context);
    }

    private void initCamera(Context context) {
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        mCameraId = null;
        try {
            mCameraId = mCameraManager.getCameraIdList()[0]; // should be back camera
        } catch (Exception e) {}
    }

    @Override
    public void turnOn() {
        try {
            mCameraManager.setTorchMode(mCameraId, true);   //Turn ON
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void turnOff() {
        try {
            mCameraManager.setTorchMode(mCameraId, false);   //Turn OFF
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {

    }
}
