package com.example.alexboicko.v_jet_test_task.Model.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.User;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DataBaseManager extends VjetDataBase{

    private static DataBaseManager instance;

    private DataBaseManager(Context context) {
        super(context);
    }

    public static DataBaseManager getInstance(Context context){
        if(instance == null)
            synchronized (DataBaseManager.class){
                if(instance == null) {
                    instance = new DataBaseManager(context);
                }
            }
            return instance;
    }


    public boolean addRowToUserTable(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER.USER_ID,user.getId());
        contentValues.put(USER.USER_NAME,user.getName());
        contentValues.put(USER.USER_EMAIL,user.getEmail());
        contentValues.put(USER.USER_AVATAR,user.getPicture().toString());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        boolean createSuccessful = sqLiteDatabase.insert(VjetDataBase.NAME_TABEL_USER,null,contentValues) > 0;
        sqLiteDatabase.close();

        return createSuccessful;
    }
    public boolean addRowToNewsDataTable(Item item, String userId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS.USER_ID,userId);
        contentValues.put(NEWS.NEWS,DataBaseUtils.toGson(item));

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        boolean createSuccessful = sqLiteDatabase.insert(VjetDataBase.NAME_TABLE_NEWS,null,contentValues) > 0;
        Log.d("MY_TAG","Add row to news: " + createSuccessful + " UserIdInput: " + userId);
        sqLiteDatabase.close();
        return createSuccessful;
    }

    public User getUserById(String userId){

        User user = null;
        String sql = "SELECT * FROM " + VjetDataBase.NAME_TABEL_USER + " WHERE " + USER.USER_ID + " = " + userId + ";";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(sql,null);
        if(c != null) {
            if (c.moveToFirst()){
                do {
                    try {
                        user.setId(c.getString(c.getColumnIndex(USER.USER_ID)));
                        user.setName(c.getString(c.getColumnIndex(USER.USER_NAME)));
                        user.setEmail(c.getString(c.getColumnIndex(USER.USER_EMAIL)));
                        user.setPicture(new URL(c.getString(c.getColumnIndex(USER.USER_AVATAR))));
                    } catch (MalformedURLException e) {
                       // e.printStackTrace();
                    }
                }while (c.moveToNext());
            }
        }
        c.close();
        sqLiteDatabase.close();
        return user;
    }

    public List<Item> getListItemByUserId(String userId){
        Log.d("MY_TAG", "Start read favorites items from db");
        Log.d("MY_TAG","UserId: " + userId);
        List<Item> itemList = new ArrayList<>();
        String sql = "SELECT " + NEWS.NEWS +" FROM " + VjetDataBase.NAME_TABLE_NEWS + " WHERE " + NEWS.USER_ID + " IN " +
                "(SELECT " + USER.USER_ID + " FROM " + VjetDataBase.NAME_TABEL_USER + " WHERE " + USER.USER_ID + " = "+userId+" );";



        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(sql,null);
        if(c != null) {
            Log.d("MY_TAG", "Cursor in getListItemByUserId: " + c);
            Log.d("MY_TAG", "Cursor move to First " + c.moveToFirst());
            Log.d("MY_TAG", "Cursor move to next " + c.moveToNext());

            if (c.moveToFirst()){

                do {

                    String item = c.getString(c.getColumnIndex(NEWS.NEWS));
                    Log.d("My_TAG", "Item from DB_Manager: " +  item);
                    Item item1 = DataBaseUtils.fromJson(item);

                    itemList.add(item1);
                }while (c.moveToNext());
            }
        }
        Log.d("MY_TAG","ListItem from DB Manager: " + itemList.size());
        c.close();
        sqLiteDatabase.close();
        return itemList;
    }


}
