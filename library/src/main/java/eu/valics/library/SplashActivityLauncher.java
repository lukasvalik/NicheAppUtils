package eu.valics.library;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by L on 9/7/2016.
 */
public class SplashActivityLauncher {

    private static final String TAG = "BG_AD_LAUNCHER";

    public static void launch(Context context){
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void launchWorkaround(Context context) throws PackageManager.NameNotFoundException {
        final String packageName = context.getPackageName();
        Log.d(TAG, "packageName: "+ packageName);
        final Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            Log.d(TAG, "launchIntent: not null");
            final String mainActivity = launchIntent.getComponent().getClassName();
            Log.d(TAG, "mainActivity: " + mainActivity);
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(packageName, mainActivity));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.getComponent() == null)
                Log.d(TAG, "launch: component == null");
            context.startActivity(intent);
            Log.d(TAG, "after launch: ");
        } else Log.d(TAG, "launchIntent: is null");
    }
}
