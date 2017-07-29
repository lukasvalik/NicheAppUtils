package eu.valics.library.Utils.permissionmanagement;

import android.app.Activity;
import android.content.Context;

import eu.valics.library.Base.AppInfo;

/**
 * Created by L on 7/7/2017.
 */

public abstract class BasePermission {

    protected boolean mDenied = false;
    protected boolean mFatal = false;
    protected boolean mNeverAskAgain = false;
    protected String mFatalDialogTitle = "";
    protected String mFatalDialogMessage = "";
    protected AppInfo mAppInfo;

    public BasePermission(AppInfo appInfo) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mAppInfo = appInfo; // never asked again marked
        } else
            mDenied = false;
    }

    abstract void askForPermission(Activity activity); //return granted?

    abstract boolean isEnabled(Context context);

    protected abstract boolean shouldAskRationale(Activity activity);

    protected abstract boolean wasAskedFirstTime(AppInfo appInfo);

    protected abstract int getRequestCode();

    protected abstract String getTitle();

    protected abstract boolean shouldNeverAskAgain(Activity activity);

    public boolean isFatal() {
        return mFatal;
    }

    public void setFatal(boolean fatal, String fatalDialogTitle, String fatalDialogMessage) {
        mFatal = fatal;
        mFatalDialogTitle = fatalDialogTitle;
        mFatalDialogMessage = fatalDialogMessage;
    }

    public boolean isDenied() {
        return mDenied;
    }

    public void setDenied(boolean denied) {
        mDenied = denied;
    }

    public String getFatalDialogTitle() {
        return mFatalDialogTitle;
    }

    public String getFatalDialogMessage() {
        return mFatalDialogMessage;
    }

    public static class RequestCode {
        /**
         * Dangerous Permissions
         */
        //Calendar
        public static final int READ_CALENDAR = 10;
        public static final int WRITE_CALENDAR = 11;

        //Camera
        public static final int CAMERA = 12;

        //Contacts
        public static final int READ_CONTACTS = 13;
        public static final int WRITE_CONTACTS = 14;
        public static final int GET_CONTACTS = 15;

        //Location
        public static final int ACCESS_FINE_LOCATION = 16;
        public static final int ACCESS_COURSE_LOCATION = 17;

        //Microphone
        public static final int MICROPHONE = 18;

        //Phone
        public static final int READ_PHONE_STATE = 19;
        public static final int CALL_PHONE = 20;
        public static final int READ_CALL_LOG = 21;
        public static final int WRITE_CALL_LOG = 22;
        public static final int ADD_VOICEMAIL = 23;
        public static final int USE_SIP = 24;
        public static final int PROCESS_OUTGOING_CALLS = 25;

        //Sensors
        public static final int BODY_SENSORS = 26;

        //Sms
        public static final int SEND_SMS = 27;
        public static final int RECEIVE_SMS = 28;
        public static final int READ_SMS = 29;
        public static final int SEND_WAP_PUSH = 30;
        public static final int SEND_MMS = 31;

        //Storage
        public static final int READ_EXTERNAL_STORAGE = 32;
        public static final int WRITE_EXTERNAL_STORAGE = 33;

        public static final int RECORD_AUDIO = 34;

        /**
         * Settings Permissions
         */
        public static final int NOTIFICATION_ACCESS = 100;
    }
}
