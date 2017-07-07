package eu.valics.library.Utils.FlashManagement;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;

/**
 * Created by L on 7/7/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FlashManagerLollipop implements FlashManager {

    private Camera mCamera;
    private Camera.Parameters mParameters;
    private SurfaceTexture mPreviewTexture;

    public FlashManagerLollipop() {
        mCamera = Camera.open();
        mParameters = mCamera.getParameters();
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(mParameters);
        mPreviewTexture = new SurfaceTexture(0);
        try {
            mCamera.setPreviewTexture(mPreviewTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
