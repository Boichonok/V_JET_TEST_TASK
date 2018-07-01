package com.example.alexboicko.v_jet_test_task.SharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.UserBundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

public class SPreferences {

    public final static void saveLastUser(Activity activity,String jsonObject){
        SharedPreferences preferences = activity.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lastUser",jsonObject);
        editor.commit();
    }

    public final static Bundle loadLastUser(Activity activity){

        SharedPreferences preferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Bundle bundle = new Bundle();
        try {
            JSONObject object = new JSONObject(preferences.getString("lastUser", ""));

            bundle.putString(UserBundle.LAST_USER.id,object.getString("id"));
            bundle.putString(UserBundle.LAST_USER.name,object.getString("name"));
            bundle.putString(UserBundle.LAST_USER.email,object.getString("email"));
            bundle.putString(UserBundle.LAST_USER.avatar,new URL(object.getJSONObject("picture").
                    getJSONObject("data").getString("url")).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bundle;
    }
}
