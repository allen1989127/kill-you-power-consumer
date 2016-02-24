
package org.sz.killyou.option;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimeOption {

    private final String TIME_OPTION_FILE = "time_option";
    public static final int DEFAULT_TIME = 10;

    private static TimeOption mInstance;

    private int mTime = -1;

    private Context mContext;

    private TimeOption(Context context) {
        mContext = context;
    }

    public void setTime(int time) {
        try {
            setTimePhy(time);
            mTime = time;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTimePhy(int time) throws IOException {
        FileOutputStream fos = mContext.openFileOutput(TIME_OPTION_FILE, 0);
        fos.write(time);
        fos.close();
    }

    public int getTime() {
        if (mTime <= 0) {
            try {
                mTime = getTimePhy();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mTime <= 0) {
                mTime = DEFAULT_TIME;
            }
        }

        return mTime;
    }

    private int getTimePhy() throws IOException {
        int ret = -1;

        FileInputStream fis = null;
        fis = mContext.openFileInput(TIME_OPTION_FILE);

        ret = fis.read();
        fis.close();

        return ret;
    }

    public synchronized static TimeOption getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TimeOption(context);
        }

        return mInstance;
    }
}
