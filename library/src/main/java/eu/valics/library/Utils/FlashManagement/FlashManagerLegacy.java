package eu.valics.library.Utils.FlashManagement;

import android.hardware.Camera;

/**
 * Created by L on 7/7/2017.
 */

public class FlashManagerLegacy implements FlashManager {

    private Camera mCamera;
    private Camera.Parameters mParameters;

    public FlashManagerLegacy(){
        initCamera();
    }

    private void initCamera() {
        mCamera = Camera.open();
        mParameters = mCamera.getParameters();
    }

    @Override
    public void turnOn() {
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(mParameters);
        mCamera.startPreview();
    }

    @Override
    public void turnOff() {
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(mParameters);
        mCamera.stopPreview();
    }

    @Override
    public void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
