
package org.sz.killyou.utils;

import android.content.pm.PackageInfo;

import org.sz.killyou.activity.ListPackages.PackageInfoShow;

import java.util.ArrayList;
import java.util.List;

public class PackageFilter {

    public static class FilterResult {
        public List<String> removeItems;
        public List<PackageInfoShow> showItems;
    }

    public static FilterResult filter(List<PackageInfo> pkgItems, List<String> selectedItems) {
        FilterResult result = new FilterResult();
        result.removeItems = new ArrayList<String>();
        result.showItems = new ArrayList<PackageInfoShow>();

        boolean existed = false;
        for (String selectedItem : selectedItems) {
            for (PackageInfo pkgItem : pkgItems) {
                if (selectedItem.equals(pkgItem.packageName)) {
                    PackageInfoShow pis = new PackageInfoShow();
                    pis.info = pkgItem;
                    pis.checked = true;
                    result.showItems.add(pis);
                    existed = true;
                }
            }

            if (!existed) {
                result.removeItems.add(selectedItem);
            }

            existed = false;
        }

        for (PackageInfoShow removeItem : result.showItems) {
            pkgItems.remove(removeItem.info);
        }

        for (PackageInfo pkgItem : pkgItems) {
            PackageInfoShow pis = new PackageInfoShow();
            pis.checked = false;
            pis.info = pkgItem;
            result.showItems.add(pis);
        }

        if (result.removeItems.size() == 0) {
            result.removeItems = null;
        }

        return result;
    }
}
