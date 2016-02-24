
package org.sz.killyou.option;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageOption {
    private final String DB_NAME = "options.db";
    private final String TABLE_NAME = "package_option";

    public static final int OPTION_SYSTEM = 0;
    public static final int OPTION_CUSTOMER = 1;

    private static PackageOption mInstance;
    private SQLiteDatabase mDb;

    private PackageOption(Context context) {
        mDb = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        boolean existed = checkTable();
        if (!existed) {
            createTable();
        }
    }

    private void createTable() {
        String sql = "create table " + TABLE_NAME
                + "(id integer primary key autoincrement, name text, category integer)";
        mDb.execSQL(sql);
    }

    public void removeItems(@NonNull
    List<String> pkgs) {
        for (String pkg : pkgs) {
            String[] args = {
                    pkg
            };

            mDb.delete(TABLE_NAME, "name=?", args);
        }
    }

    public void addItems(@NonNull
    Map<String, Integer> pkgs) {
        for (String pkg : pkgs.keySet()) {
            ContentValues cv = new ContentValues();
            cv.put("name", pkg);
            cv.put("category", pkgs.get(pkg));

            mDb.insert(TABLE_NAME, null, cv);
        }
    }

    public List<String> getItems(int option) {
        boolean legal = false;
        switch (option) {
            case OPTION_SYSTEM:
            case OPTION_CUSTOMER:
                legal = true;
                break;
        }
        
        if (!legal) {
            return null;
        }
        
        String[] args = {
                Integer.toString(option),
        };

        Cursor c = mDb.query(TABLE_NAME, new String[] {
                "name",
        }, "category=?", args, null, null, null);

        List<String> items = new ArrayList<String>();

        while (c.moveToNext()) {
            items.add(c.getString(0));
        }
        
        c.close();

        return items;
    }

    private boolean checkTable() {
        boolean existed = false;

        String[] args = {
                "table",
                TABLE_NAME,
        };

        Cursor c = mDb.rawQuery(
                "SELECT name FROM sqlite_master WHERE type=? and name=?",
                args);

        if (c.moveToNext()) {
            existed = true;
        }

        c.close();
        return existed;
    }

    public synchronized static PackageOption getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PackageOption(context);
        }

        return mInstance;
    }
}
