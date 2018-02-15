package com.software.amazing.weatherassist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by myxroft on 08/02/2018.
 */

public class EnvironmentDB extends SQLiteOpenHelper {

    public EnvironmentDB(Context context) {
        super(context, "env_db.db", null, 1);
    }

    protected class EnvironmentTable{

        public static final String TABLE_NAME = "tbl_environment";

        public static final String ID = "_id";

        public static final String TEMPERATURE_VALUE = "env_temp";

        public static final String HUMIDITY_VALUE = "env_humd";

        public static final String BRIGHTNESS_VALUE = "env_bright";

        public static final String UV_VALUE = "env_uv";

        public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TEMPERATURE_VALUE + " INTEGER,"
                + HUMIDITY_VALUE + " INTEGER,"
                + BRIGHTNESS_VALUE + " INTEGER,"
                + UV_VALUE + " INTEGER);";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(EnvironmentTable.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
