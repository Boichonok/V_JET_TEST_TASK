package com.example.alexboicko.v_jet_test_task.Model.DataBase;

import android.util.Log;

import com.example.alexboicko.v_jet_test_task.Model.Pojo.NewsAPI.Article;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;
import com.example.alexboicko.v_jet_test_task.ViewModel.Adapters.ItemAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class DataBaseUtils {
    public static String toGson(Item item) {
        Gson gson = new Gson();
        return gson.toJson(item);
    }
    public static Item fromGSON(String item){
        Gson gson = new Gson();
        Type token = new TypeToken<Item>(){}.getType();
        Log.d("MY_TAG", gson.fromJson(item,token));

        return gson.fromJson(item,token);
    }

    public static Item fromJson(String item) {
        Item item1 = null;
        try {
            JSONObject jsonObject = new JSONObject(item);
            JSONObject article = jsonObject.getJSONObject("article");
            Gson gson = new Gson();
            Article article1 = gson.fromJson(article.toString(),Article.class);
            item1 = new ItemAdapter(article1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item1;
    }
}
