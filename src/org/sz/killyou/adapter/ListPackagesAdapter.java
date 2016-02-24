
package org.sz.killyou.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sz.killyou.R;
import org.sz.killyou.activity.ListPackages.PackageInfoShow;

import java.util.List;

public class ListPackagesAdapter extends BaseAdapter {

    static class ViewHolder
    {
        public ImageView appIcon;
        public TextView appName;
        public TextView packageName;
    }

    private LayoutInflater mInflater;
    private List<PackageInfoShow> mPackages;
    private Context mContext;

    public ListPackagesAdapter(Context context, List<PackageInfoShow> infos) {
        mInflater = LayoutInflater.from(context);
        mPackages = infos;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPackages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.layout_package_show, null);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.appicon);
            holder.packageName = (TextView) convertView.findViewById(R.id.packageName);
            holder.appName = (TextView) convertView.findViewById(R.id.appName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PackageManager pm = mContext.getPackageManager();
        PackageInfoShow info = mPackages.get(position);
        ApplicationInfo appInfo = info.info.applicationInfo;

        holder.appIcon.setImageDrawable(pm.getApplicationIcon(appInfo));
        holder.appIcon.setBackgroundColor(info.checked ? Color.GREEN : Color.DKGRAY);
        holder.appName.setText(pm.getApplicationLabel(appInfo));
        holder.packageName.setText(appInfo.packageName);

        return convertView;
    }

    public void updateData(List<PackageInfoShow> data) {
        mPackages = data;
    }

}
