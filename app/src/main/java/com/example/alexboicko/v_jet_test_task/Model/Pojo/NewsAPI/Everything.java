package com.example.alexboicko.v_jet_test_task.Model.Pojo.NewsAPI;

import java.util.ArrayList;

public class Everything {
    private String status;
    private int totalResults;
    private ArrayList<Article> articles;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }
}
