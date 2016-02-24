
package org.sz.killyou.service.action;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class KillerAction implements Action {
    private final String TAG = "KillerAction";

    private List<String> mApps;

    private Context mContext;

    public static class KillActionParam {
        public List<String> apps;
        public Context context;
    }

    public KillerAction(KillActionParam params) {
        mApps = params.apps;
        mContext = params.context.getApplicationContext();
    }

    @Override
    public void execute(Process proc) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;

        if (currentAPIVersion <= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<RunningAppProcessInfo> taskInfos = am.getRunningAppProcesses();

            // current running app, not force stop it
            RunningAppProcessInfo currentProc = taskInfos.get(0);
            String currentProcName = currentProc.processName.split(":")[0];
            taskInfos.remove(0);

            try {
                OutputStreamWriter osw = new OutputStreamWriter(proc.getOutputStream());

                for (String app : mApps) {
                    if (currentProcName.startsWith(app)) {
                        continue;
                    }

                    for (RunningAppProcessInfo taskInfo : taskInfos) {
                        if (taskInfo.processName.startsWith(app)) {
                            String cmd = "am force-stop " + app + "\n";
                            osw.write(cmd);
                            osw.flush();

                            Log.d(TAG, cmd);
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<UsageStats> queryUsageStats = usageEnabled();

            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                return;
            } else {
                Log.d(TAG, "queryUsageStats is enable");

                SortedMap<Long, UsageStats> sortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : queryUsageStats) {
                    sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }

                String currentProcName = null;

                if (sortedMap != null && !sortedMap.isEmpty()) {
                    currentProcName = sortedMap.get(sortedMap.lastKey()).getPackageName();
                }

                Log.d(TAG, "current proc name is " + currentProcName);

                List<RunningServiceInfo> runningServices = am.getRunningServices(Integer.MAX_VALUE);
                try {
                    OutputStreamWriter osw = new OutputStreamWriter(proc.getOutputStream());

                    for (String app : mApps) {
                        if (currentProcName.startsWith(app)) {
                            continue;
                        }

                        for (RunningServiceInfo runningService : runningServices) {
                            if (runningService.process.startsWith(app)) {
                                String cmd = "am force-stop " + app + "\n";
                                osw.write(cmd);
                                osw.flush();

                                Log.d(TAG, cmd);
                                break;
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<UsageStats> usageEnabled() {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) mContext
                .getApplicationContext().getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);

        return queryUsageStats;
    }

}
