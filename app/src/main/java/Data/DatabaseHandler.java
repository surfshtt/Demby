package Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import Utils.ProfilesUtil;
import Utils.UsersUtil;
import Utils.Util;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTE_TABLE = "CREATE TABLE " + UsersUtil.TABLE_NAME + " ("
                + UsersUtil.KEY_ID + " INTEGER PRIMARY KEY, "
                + UsersUtil.KEY_USERNAME + " TEXT, "
                + UsersUtil.KEY_EMAIL + " TEXT, "
                + UsersUtil.KEY_PASSWORD + " TEXT)";

        db.execSQL(CREATE_NOTE_TABLE);
        Log.d("DB", "Таблица " + UsersUtil.TABLE_NAME +" создана");


        CREATE_NOTE_TABLE = "CREATE TABLE " + ProfilesUtil.TABLE_NAME + " ("
                + ProfilesUtil.KEY_ID + " INTEGER PRIMARY KEY, "
                + ProfilesUtil.KEY_OWNER_ID + " INTEGER, "
                + ProfilesUtil.KEY_REAL_NAME + " TEXT, "
                + ProfilesUtil.KEY_AGE + " INT, "
                + ProfilesUtil.KEY_CITY + " TEXT, "
                + ProfilesUtil.KEY_DESCRIPTION+ " TEXT, "
                + ProfilesUtil.KEY_SEEN_BY + " TEXT, "
                + ProfilesUtil.KEY_LIKED_BY + " TEXT)";

        db.execSQL(CREATE_NOTE_TABLE);
        Log.d("DB", "Таблица " + UsersUtil.TABLE_NAME +" создана");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersUtil.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProfilesUtil.TABLE_NAME);

        onCreate(db);
    }
}
