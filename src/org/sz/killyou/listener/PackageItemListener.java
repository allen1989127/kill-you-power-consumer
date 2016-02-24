
package org.sz.killyou.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import org.sz.killyou.activity.ListPackages.PackageInfoShow;
import org.sz.killyou.adapter.ListPackagesAdapter;
import org.sz.killyou.option.PackageOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageItemListener implements OnItemClickListener {

    private int mType = PackageOption.OPTION_CUSTOMER;
    private ListPackagesAdapter mAdapter;
    private List<PackageInfoShow> mInfo;
    private Context mContext;

    public PackageItemListener(Context context, int type, ListPackagesAdapter adapter,
            List<PackageInfoShow> info) {
        mType = type;
        mAdapter = adapter;
        mInfo = info;
        mContext = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PackageInfoShow clickedItem = mInfo.get(position);
        if (!clickedItem.checked) {
            clickedItem.checked = true;
            mInfo.remove(position);
            mInfo.add(0, clickedItem);

            Map<String, Integer> addItem = new HashMap<String, Integer>();
            addItem.put(clickedItem.info.packageName, mType);
            PackageOption.getInstance(mContext.getApplicationContext()).addItems(addItem);

            mAdapter.updateData(mInfo);
            mAdapter.notifyDataSetChanged();
        } else {
            clickedItem.checked = false;
            mInfo.remove(position);
            mInfo.add(mInfo.size(), clickedItem);

            List<String> delItem = new ArrayList<String>();
            delItem.add(clickedItem.info.packageName);
            PackageOption.getInstance(mContext.getApplicationContext()).removeItems(delItem);

            mAdapter.updateData(mInfo);
            mAdapter.notifyDataSetChanged();
        }
    }

}
