package com.example.alexboicko.v_jet_test_task.ViewModel.Adapters;

import com.example.alexboicko.v_jet_test_task.Model.Pojo.NewsAPI.Article;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class ItemAdapter  implements Item, Serializable{

    private Article article;



    public ItemAdapter(Article article){
        this.article = article;
    }




    @Override
    public int getId() {
        return Integer.valueOf(article.getSource().getId());
    }

    @Override
    public String getSourceName() {
        return article.getSource().getName();
    }

    @Override
    public String getAuthor() {
        return article.getAuthor();
    }

    @Override
    public String getTitle() {
        return  article.getTitle();
    }

    @Override
    public String getDescription() {
        return article.getDescription();
    }

    @Override
    public URL getUrl() {
        URL articleUrl = null;
        try {
            articleUrl  = new URL(article.getUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return articleUrl;
    }

    @Override
    public URL getImageToArticle() {
        URL imageURL = null;
        try {
            imageURL  = new URL(article.getUrlToImage());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return imageURL;
    }

    @Override
    public String getPublishAt() {
        return article.getPublishedAt();
    }


}
