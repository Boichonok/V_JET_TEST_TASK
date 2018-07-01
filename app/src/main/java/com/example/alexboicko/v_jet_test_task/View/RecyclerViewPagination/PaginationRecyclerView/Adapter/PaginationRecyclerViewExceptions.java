package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Adapter;

public class PaginationRecyclerViewExceptions extends RuntimeException {
    public PaginationRecyclerViewExceptions() {
        super("Exception in AutoLoadingRecyclerView");
    }

    public PaginationRecyclerViewExceptions(String detailMessage) {
        super(detailMessage);
    }
}
