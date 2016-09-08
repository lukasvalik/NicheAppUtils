package eu.valics.library;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by L on 9/7/2016.
 */
public class SplashActivityLauncher {

    public static void launch(Context context) throws PackageManager.NameNotFoundException {
        final String packageName = context.getPackageName();
        final Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            final String mainActivity = launchIntent.getComponent().getClassName();
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(packageName, mainActivity));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
