
package org.sz.killyou.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import org.sz.killyou.R;
import org.sz.killyou.option.PackageOption;
import org.sz.killyou.option.TimeOption;
import org.sz.killyou.service.action.Action;
import org.sz.killyou.service.action.ActionFactory;
import org.sz.killyou.service.action.ActionFactory.ActionType;
import org.sz.killyou.service.action.KillerAction.KillActionParam;

import java.util.List;

public class KillService extends Service {

    private final String TAG = "KillService";

    private final int KILL_OPERATE = 0;
    private final int NOTIFICATIN_ID = 23;

    private Notification mNotification;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("kill-thread");
        thread.start();

        final Handler handler = new Handler(thread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int delay = TimeOption.DEFAULT_TIME;
                KillActionParam killParam;

                switch (msg.what) {
                    case KILL_OPERATE:
                        delay = TimeOption.getInstance(getApplicationContext()).getTime();
                        List<String> customerApps = PackageOption.getInstance(
                                getApplicationContext()).getItems(PackageOption.OPTION_CUSTOMER);
                        List<String> systemApps = PackageOption.getInstance(
                                getApplicationContext()).getItems(PackageOption.OPTION_SYSTEM);
                        if (customerApps == null || systemApps == null) {
                            Log.w(TAG, "customer apps or system apps is null");
                            break;
                        }

                        customerApps.addAll(systemApps);

                        if (customerApps.size() == 0) {
                            Log.d(TAG, "size of apps is 0");
                            break;
                        }
                        Process proc = ActionFactory.initProc();
                        killParam = new KillActionParam();
                        killParam.context = getApplicationContext();
                        killParam.apps = customerApps;
                        Action action = ActionFactory.createAction(ActionType.KILLER, killParam);
                        action.execute(proc);
                        break;
                    default:
                        Log.w(TAG, "Error message : " + msg.what);
                        return;
                }

                this.sendEmptyMessageDelayed(msg.what, delay * 1000);
            }

        };

        handler.sendEmptyMessage(KILL_OPERATE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mNotification == null) {
            mNotification = new Notification();
            
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.flags |=Notification.FLAG_ONGOING_EVENT;
            
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.layout_notification);

            contentView.setImageViewResource(R.id.icon, R.drawable.ic_launcher);

            mNotification.contentView = contentView;
        }
        
        startForeground(NOTIFICATIN_ID, mNotification);
        
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

}
