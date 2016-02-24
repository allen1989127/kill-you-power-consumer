
package org.sz.killyou.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class PackageLoader {

    private enum PackageClass {
        ALL, CUSTOMER, SYSTEM,
    }

    public static List<PackageInfo> getApps(Context context, PackageClass cls) {
        List<PackageInfo> infos = new ArrayList<PackageInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        switch (cls) {
            case CUSTOMER:
                for (PackageInfo info : packages) {
                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        infos.add(info);
                    }
                }
                break;
            case SYSTEM:
                for (PackageInfo info : packages) {
                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        infos.add(info);
                    }
                }
                break;
            case ALL:
                for (PackageInfo info : packages) {
                    infos.add(info);
                }
                break;
            default:
                return null;
        }

        return infos;
    }

    public static List<PackageInfo> getSystemApps(Context context) {
        return getApps(context, PackageClass.SYSTEM);
    }

    public static List<PackageInfo> getCustomerApps(Context context) {
        return getApps(context, PackageClass.CUSTOMER);
    }

    public static List<PackageInfo> getAllApps(Context context) {
        return getApps(context, PackageClass.ALL);
    }
}
