
package org.sz.killyou.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.sz.killyou.R;
import org.sz.killyou.listener.PackageTabListener;
import org.sz.killyou.option.TimeOption;
import org.sz.killyou.service.KillService;

public class ListPackages extends Activity {

    public static class PackageInfoShow {
        public PackageInfo info;
        public boolean checked = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.setContentView(R.layout.layout_main);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab customerTab = actionBar.newTab().setText(
                this.getResources().getString(R.string.customer_tab));
        final Fragment customerPackageList = new CustomerPackageFragment(this);
        customerTab.setTabListener(new PackageTabListener(customerPackageList));

        Tab systemTab = actionBar.newTab().setText(
                this.getResources().getString(R.string.system_tab));
        final Fragment systemPackageList = new SystemPackageFragment(this);
        systemTab.setTabListener(new PackageTabListener(systemPackageList));

        actionBar.addTab(customerTab);
        actionBar.addTab(systemTab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.startService(new Intent(ListPackages.this, KillService.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_time:
                LayoutInflater inflater = LayoutInflater.from(this);
                View setTimeView = inflater.inflate(R.layout.layout_set_time, null);

                final EditText etSetTime = (EditText) setTimeView.findViewById(R.id.et_set_time);
                Dialog dialog = new AlertDialog.Builder(this).setTitle(R.string.set_time_title)
                        .setView(setTimeView).setPositiveButton(R.string.ok, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String timeStr = etSetTime.getText().toString();
                                int time = Integer.parseInt(timeStr);
                                if (time < 1 || time > 255) {
                                    Toast.makeText(getApplicationContext(),
                                            R.string.set_time_error_tip, Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    TimeOption.getInstance(getApplicationContext()).setTime(time);
                                }
                            }

                        }).setNegativeButton(R.string.cancel, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }

                        }).create();

                dialog.show();
                break;
            case R.id.kill_application:
                this.stopService(new Intent(ListPackages.this, KillService.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
