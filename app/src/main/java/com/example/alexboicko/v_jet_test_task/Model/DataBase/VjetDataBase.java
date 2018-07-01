package com.example.alexboicko.v_jet_test_task.Model.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VjetDataBase extends SQLiteOpenHelper {

    public final static String NAME_DATA_BASE = "V_JetDb";
    public final static int VERSION = 1;

    public final static String NAME_TABLE_NEWS = "NEWS";
    public final static String NAME_TABEL_USER = "USER";

    public final static String INDEX = "id";



    public static class NEWS{
        public final static String NEWS = "NEWS";
        public final static String USER_ID = "NEWS_USER_ID";
    }

    public static class USER {
        public final static String USER_EMAIL = "EMAIL";
        public final static String USER_NAME  = "NAME";
        public final static String USER_AVATAR = "AVATAR";
        public final static String USER_ID = "USER_ID";
    }



    private boolean isDropUserTable;
    private boolean isDropNewsTable;



    public VjetDataBase(Context context){
        super(context,NAME_DATA_BASE,null,VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = on;");
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
        NAME_TABEL_USER + " (" +
        INDEX + " INTEGER PRIMARY KEY, " +
        USER.USER_ID + " TEXT NOT NULL, "+
                USER.USER_NAME + " TEXT NOT NULL, " +
                USER.USER_EMAIL + " TEXT NOT NULL, " +
                USER.USER_AVATAR + " TEXT NOT NULL);");


        db.execSQL("CREATE TABLE IF NOT EXISTS " +
        NAME_TABLE_NEWS + " (" +
        INDEX + " INTEGER PRIMARY KEY, " +
        NEWS.NEWS + " TEXT NOT NULL, " +
                NEWS.USER_ID + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + NEWS.USER_ID + ") REFERENCES " + NAME_TABEL_USER + "("+USER.USER_ID+") );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + NAME_DATA_BASE + ";");
        isDropNewsTable = dropTableDataBase(db,NAME_TABLE_NEWS);
        isDropUserTable = dropTableDataBase(db,NAME_TABEL_USER);
    }

    public boolean dropTableDataBase(SQLiteDatabase sqLiteDatabase, String NAME_TABLE){
        boolean isDrop = false;
        try {
            if (sqLiteDatabase.query(NAME_TABLE,null,null,null,null,null,INDEX).getCount() != 0) {
                isDrop = false;
            } else {
                isDrop = true;
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return isDrop;
    }

    public boolean isDropNewsTable() {
        return isDropNewsTable;
    }

    public boolean isDropUserTable() {
        return isDropUserTable;
    }
}
