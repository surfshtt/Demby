package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.dembyapp.ProfileFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Model.ArchiveMutual;
import Model.Profile;
import Model.User;
import Utils.ArchiveMutualUtil;
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
                + ProfilesUtil.KEY_OWNER_NAME + " TEXT, "
                + ProfilesUtil.KEY_REAL_NAME + " TEXT, "
                + ProfilesUtil.KEY_GENDER + " TEXT, "
                + ProfilesUtil.KEY_GENDER_LOOKING + " TEXT, "
                + ProfilesUtil.KEY_AGE + " INT, "
                + ProfilesUtil.KEY_CITY + " TEXT, "
                + ProfilesUtil.KEY_DESCRIPTION+ " TEXT, "
                + ProfilesUtil.KEY_SEEN_BY + " TEXT, "
                + ProfilesUtil.KEY_LIKED_BY + " TEXT, "
                + ProfilesUtil.KEY_IMAGE + " BLOB, "
                + ProfilesUtil.KEY_INSTAGRAM + " TEXT, "
                + ProfilesUtil.KEY_TELEGRAM + " TEXT)";

        db.execSQL(CREATE_NOTE_TABLE);
        Log.d("DB", "Таблица " + ProfilesUtil.TABLE_NAME +" создана");

        CREATE_NOTE_TABLE = "CREATE TABLE " + ArchiveMutualUtil.TABLE_NAME + " ("
                + ArchiveMutualUtil.KEY_ID + " INTEGER PRIMARY KEY, "
                + ArchiveMutualUtil.KEY_OWNER_NAME + " TEXT, "
                + ArchiveMutualUtil.KEY_MUTUAL_NAMES + " TEXT)";

        db.execSQL(CREATE_NOTE_TABLE);
        Log.d("DB", "Таблица " + ArchiveMutualUtil.TABLE_NAME +" создана");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersUtil.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProfilesUtil.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ArchiveMutualUtil.TABLE_NAME);

        onCreate(db);
        Log.d("DB", "БД " + Util.DATABASE_NAME +" обновлена");
    }

    //Работа с аккаунатами
    public void newUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsersUtil.KEY_USERNAME, user.getUsername());
        contentValues.put(UsersUtil.KEY_EMAIL, user.getEmail());
        contentValues.put(UsersUtil.KEY_PASSWORD, user.getPassword());

        db.insert(UsersUtil.TABLE_NAME, null, contentValues);
        db.close();
    }

    public User getUser(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UsersUtil.TABLE_NAME,new String[] {UsersUtil.KEY_ID, UsersUtil.KEY_USERNAME, UsersUtil.KEY_EMAIL,UsersUtil.KEY_PASSWORD},
                UsersUtil.KEY_USERNAME + "=?", new String[]{userName}, null,null,null,null);

        if(cursor != null){
            try {
                cursor.moveToFirst();

                return new User(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UsersUtil.TABLE_NAME,
                new String[]{UsersUtil.KEY_ID, UsersUtil.KEY_USERNAME, UsersUtil.KEY_EMAIL, UsersUtil.KEY_PASSWORD},
                null, null, null, null, null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    User user = new User(
                            Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)
                    );
                    userList.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return userList;
    }

    public Profile getProfileByName(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ProfilesUtil.TABLE_NAME, new String[]{
                ProfilesUtil.KEY_ID,
                ProfilesUtil.KEY_OWNER_NAME,
                ProfilesUtil.KEY_REAL_NAME,
                ProfilesUtil.KEY_GENDER,
                ProfilesUtil.KEY_GENDER_LOOKING,
                ProfilesUtil.KEY_AGE,
                ProfilesUtil.KEY_CITY,
                ProfilesUtil.KEY_DESCRIPTION,
                ProfilesUtil.KEY_SEEN_BY,
                ProfilesUtil.KEY_LIKED_BY,
                ProfilesUtil.KEY_IMAGE,
                ProfilesUtil.KEY_INSTAGRAM,
                ProfilesUtil.KEY_TELEGRAM
        }, ProfilesUtil.KEY_OWNER_NAME + "=?", new String[]{userName}, null, null, null, null);

        if(cursor != null){
            try {
                cursor.moveToFirst();

                return new Profile(
                            Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            Integer.parseInt(cursor.getString(5)),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                            cursor.getBlob(10),
                            cursor.getString(11),
                            cursor.getString(12)
                    );
            }
            catch (Exception ex) {
                Log.e("ProfileQuery", "Error retrieving profile: " + ex.getMessage());
                return null;
            }
        }
        else{
            return null;
        }
    }

    public void newProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ProfilesUtil.KEY_OWNER_NAME, profile.getOwnerId());
        contentValues.put(ProfilesUtil.KEY_REAL_NAME, profile.getRealName());
        contentValues.put(ProfilesUtil.KEY_GENDER, profile.getGender());
        contentValues.put(ProfilesUtil.KEY_GENDER_LOOKING, profile.getGender_looking());
        contentValues.put(ProfilesUtil.KEY_AGE, profile.getAge());
        contentValues.put(ProfilesUtil.KEY_CITY, profile.getCity());
        contentValues.put(ProfilesUtil.KEY_DESCRIPTION, profile.getDescription());
        contentValues.put(ProfilesUtil.KEY_SEEN_BY, profile.getSeenBy());
        contentValues.put(ProfilesUtil.KEY_LIKED_BY, profile.getLikedBy());
        contentValues.put(ProfilesUtil.KEY_IMAGE, profile.getImage());
        contentValues.put(ProfilesUtil.KEY_INSTAGRAM, profile.getInstagram());
        contentValues.put(ProfilesUtil.KEY_TELEGRAM, profile.getTelegram());

        db.insert(ProfilesUtil.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void deleteProfile(String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProfilesUtil.TABLE_NAME,ProfilesUtil.KEY_OWNER_NAME + "=?", new String[]{userName});
        Log.d("Accounts", userName + "'s profile has successfully deleted");
        db.close();
    }

    public void deleteProfileById(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProfilesUtil.TABLE_NAME,ProfilesUtil.KEY_ID + "=?", new String[]{id});
        Log.d("Accounts", "Profile with id " + id + " has successfully deleted");
        db.close();
    }

    public int updateProfile(Profile profile){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ProfilesUtil.KEY_OWNER_NAME, profile.getOwnerId());
        contentValues.put(ProfilesUtil.KEY_REAL_NAME, profile.getRealName());
        contentValues.put(ProfilesUtil.KEY_GENDER, profile.getGender());
        contentValues.put(ProfilesUtil.KEY_GENDER_LOOKING, profile.getGender_looking());
        contentValues.put(ProfilesUtil.KEY_AGE, profile.getAge());
        contentValues.put(ProfilesUtil.KEY_CITY, profile.getCity());
        contentValues.put(ProfilesUtil.KEY_DESCRIPTION, profile.getDescription());
        contentValues.put(ProfilesUtil.KEY_SEEN_BY, profile.getSeenBy());
        contentValues.put(ProfilesUtil.KEY_LIKED_BY, profile.getLikedBy());
        contentValues.put(ProfilesUtil.KEY_IMAGE, profile.getImage());
        contentValues.put(ProfilesUtil.KEY_INSTAGRAM, profile.getInstagram());
        contentValues.put(ProfilesUtil.KEY_TELEGRAM, profile.getTelegram());

        return db.update(ProfilesUtil.TABLE_NAME,contentValues,ProfilesUtil.KEY_OWNER_NAME + "=?", new String[]{profile.getOwnerId()});
    }

    public List<Profile> getProfiles(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Profile> profilesList = new ArrayList<>();

        Profile tmpProf = getProfileByName(userName);

        if (tmpProf == null) {
            return null;
        }

        String selectAllNotes = "SELECT * FROM " + ProfilesUtil.TABLE_NAME +
                " WHERE " + ProfilesUtil.KEY_OWNER_NAME + " != ?";
        Cursor cursor = db.rawQuery(selectAllNotes, new String[]{userName});

        if (cursor.moveToFirst()) {
            do {
                profilesList.add(new Profile(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Integer.parseInt(cursor.getString(5)),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getBlob(10),
                        cursor.getString(11),
                        cursor.getString(12)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();

        if (!profilesList.isEmpty()) {
            String genderToFind = getProfileByName(userName).getGender_looking();
            String usersGender = getProfileByName(userName).getGender();
            List<Profile> filteredList = new ArrayList<>();

            for (Profile prof : profilesList) {
                if (prof.getGender().equals(genderToFind)) {
                    if(prof.getGender_looking().equals(usersGender)) {
                        filteredList.add(prof);
                    }
                }
            }

            profilesList = filteredList;
        }

        return profilesList;
    }

    //Взаимный архив

    public void newArchiveMutual(ArchiveMutual archiveMutual){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ArchiveMutualUtil.KEY_OWNER_NAME, archiveMutual.getOwnerName());
        contentValues.put(ArchiveMutualUtil.KEY_MUTUAL_NAMES, archiveMutual.getMutualNames());

        db.insert(ArchiveMutualUtil.TABLE_NAME, null, contentValues);
        db.close();
    }

    public ArchiveMutual getArchiveMutual(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ArchiveMutualUtil.TABLE_NAME,new String[] {
                    ArchiveMutualUtil.KEY_ID,
                    ArchiveMutualUtil.KEY_OWNER_NAME,
                    ArchiveMutualUtil.KEY_MUTUAL_NAMES,
                },
                ArchiveMutualUtil.KEY_OWNER_NAME + "=?", new String[]{userName},
                null,
                null,
                null,
                null);

        if(cursor != null){
            try {
                cursor.moveToFirst();

                return new ArchiveMutual(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2)
                );

            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public int updateArchive(ArchiveMutual archiveMutual){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ArchiveMutualUtil.KEY_ID, archiveMutual.getId());
        contentValues.put(ArchiveMutualUtil.KEY_OWNER_NAME, archiveMutual.getOwnerName());
        contentValues.put(ArchiveMutualUtil.KEY_MUTUAL_NAMES, archiveMutual.getMutualNames());

        return db.update(ArchiveMutualUtil.TABLE_NAME,contentValues,ArchiveMutualUtil.KEY_OWNER_NAME + "=?", new String[]{archiveMutual.getOwnerName()});
    }
}
