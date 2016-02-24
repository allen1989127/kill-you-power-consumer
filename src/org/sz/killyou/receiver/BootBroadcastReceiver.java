
package org.sz.killyou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.sz.killyou.service.KillService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "start kill service");
        Intent service = new Intent(context, KillService.class);
        context.startService(service);
    }
}
