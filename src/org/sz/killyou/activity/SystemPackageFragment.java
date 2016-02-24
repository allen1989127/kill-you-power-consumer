
package org.sz.killyou.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.sz.killyou.R;
import org.sz.killyou.adapter.ListPackagesAdapter;
import org.sz.killyou.listener.PackageItemListener;
import org.sz.killyou.option.PackageOption;
import org.sz.killyou.utils.PackageFilter;
import org.sz.killyou.utils.PackageLoader;
import org.sz.killyou.utils.PackageFilter.FilterResult;

import java.util.List;

public class SystemPackageFragment extends Fragment {
    private final String TAG = "SystemPackageFragment";

    private Context mContext;

    private List<PackageInfo> mSystemPackages;
    private List<String> mSelectedPackages;
    private ListPackagesAdapter mAdapter;
    private ListView mSystemPackageList;

    public SystemPackageFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_list_package_fragment, container, false);
        initialize(v);

        return v;
    }

    private void initialize(View v) {
        mSystemPackageList = (ListView) v.findViewById(R.id.package_list);
        Log.d(TAG, "start load system apps");
        mSystemPackages = PackageLoader.getSystemApps(mContext);
        mSelectedPackages = PackageOption.getInstance(mContext.getApplicationContext()).getItems(
                PackageOption.OPTION_SYSTEM);
        FilterResult result = PackageFilter.filter(mSystemPackages, mSelectedPackages);
        if (result.removeItems != null) {
            PackageOption.getInstance(mContext.getApplicationContext()).removeItems(
                    result.removeItems);
        }

        mAdapter = new ListPackagesAdapter(mContext, result.showItems);
        mSystemPackageList.setAdapter(mAdapter);

        mSystemPackageList.setOnItemClickListener(new PackageItemListener(
                mContext, PackageOption.OPTION_SYSTEM, mAdapter, result.showItems));
    }

}
