package eu.valics.library.FloatingIcon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by L on 9/7/2016.
 */
public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, FloatingIconService.class);
        context.startService(i);
    }
}
