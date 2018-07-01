package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item;

import android.net.Uri;

import java.net.URL;

public interface Item {

    public int getId() ;

    public String getSourceName();
    public String getAuthor();

    public String getTitle();

    public String getDescription();

    public URL getUrl();

    public URL getImageToArticle();

    public String getPublishAt();
}
