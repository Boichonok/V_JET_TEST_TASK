package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Adapter;

import android.view.View;

import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;

public interface OnItemClickListener {
    void onItemClick(View v, Item item, int position);
}
