package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginationRecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> listElements = new ArrayList<>();


    public void addNewItems(List<T> items) {
        listElements.addAll(items);
    }

    public void addNewItem(T item){listElements.add(item);}
    public void addNewItem(int index,T item){listElements.add(index,item);}


    public boolean contain(T item){
        return listElements.contains(item);
    }

    public void removeItem(int index) {listElements.remove(index);}

    public void removeItem(T item) {
        listElements.remove(item);
    }

    public List<T> getItems() {
        return listElements;
    }

    public T getItem(int position) {
        return listElements.get(position);
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }


}
