package eu.valics.library.Utils.Ads;

import android.os.Handler;

/**
 * Created by L on 9/7/2016.
 */
public class FakeLoading {

    private Handler mHandler;
    private Runnable mRunnable;

    private int count = 0;

    public FakeLoading(){
        mHandler = new Handler();
    }

    public void startLoading(final OnFinishedListener onFinishedListener){

        mRunnable = new Runnable() {
            @Override
            public void run() {
                count++;
                if(count<=300)
                    mHandler.postDelayed(this, 50);
                else {
                    onFinishedListener.onFinished();
                }
            }
        };
        mHandler.postDelayed(mRunnable, 50);
    }

    public void stopLoading(){
        mHandler.removeCallbacksAndMessages(null);
    }

    public void startHalfSecLoading(final OnFinishedListener onFinishedListener){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                count++;
                if(count<=50)
                    mHandler.postDelayed(this, 10);
                else {
                    onFinishedListener.onFinished();
                }
            }
        };
        mHandler.postDelayed(runnable, 10);
    }

    public void unregister(){

        /**
         * After closing InterstitialAd Handler is already null, so calling unregister will result
         * in NullPointerException
         */

        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
            mRunnable = null;
        }
    }
}