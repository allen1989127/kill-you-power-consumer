
package org.sz.killyou.service.action;

import android.util.Log;

import org.sz.killyou.service.action.KillerAction.KillActionParam;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ActionFactory {

    private static final String TAG = "ActionFactory";

    public enum ActionType {
        KILLER,
    }

    private static Process mProc;

    public synchronized static Process initProc() {
        if (mProc == null) {
            Runtime runtime = Runtime.getRuntime();
            try {
                mProc = runtime.exec("su");
            } catch (IOException e) {
                e.printStackTrace();
                mProc = null;
            }
        }

        return mProc;
    }

    public static Action createAction(ActionType type, Object params) {
        Action action = null;

        switch (type) {
            case KILLER:
                action = new KillerAction((KillActionParam) params);
                break;
            default:
                Log.e(TAG, "Failed to create action : " + type);
                break;
        }

        return action;
    }
}
