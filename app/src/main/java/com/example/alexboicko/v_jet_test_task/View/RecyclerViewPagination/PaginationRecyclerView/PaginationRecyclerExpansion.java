package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.DisposableSubscriber;

import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Adapter.*;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;

public class PaginationRecyclerExpansion {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishProcessor<Integer> pagination = PublishProcessor.create();


    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager linearLayoutManager;

    private PaginationRecyclerViewAdapter paginationRecyclerViewAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;




    public PaginationRecyclerExpansion(PaginationRecyclerViewAdapter paginationRecyclerViewAdapter,
                                       LinearLayoutManager linearLayoutManager,
                                       ProgressBar progressBar,
                                       RecyclerView recyclerView) {
        this.paginationRecyclerViewAdapter = paginationRecyclerViewAdapter;
        this.linearLayoutManager = linearLayoutManager;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }

    public void subscribeData(List<Item> items){

        Disposable disposable = pagination
                .onBackpressureBuffer()
                .map(new Function<Integer, Flowable<Item>>() {
                    @Override
                    public Flowable<Item> apply(Integer page) throws Exception {

                        loading = true;
                        if(page < items.size()) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        return Flowable.create(new FlowableOnSubscribe<Item>() {
                            @Override
                            public void subscribe(FlowableEmitter<Item> e) throws Exception {

                                    e.onNext(items.get(page));
                                    e.onComplete();
                            }
                        }, BackpressureStrategy.BUFFER).delay(2,TimeUnit.SECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Flowable<Item>>() {
                    @Override
                    public void accept(Flowable<Item> listFlowable) throws Exception {

                        compositeDisposable.add(listFlowable.observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<Item>() {
                            @Override
                            public void onNext(Item item) {

                                paginationRecyclerViewAdapter.addNewItem(item);
                                paginationRecyclerViewAdapter.notifyDataSetChanged();

                                loading = false;
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }));


                    }
                });
        compositeDisposable.add(disposable);
        //pagination.onNext(pageNumber);
    }


    public void setFirsPage(Item item){
        paginationRecyclerViewAdapter.addNewItem(item);
        paginationRecyclerViewAdapter.notifyDataSetChanged();
        loading = false;
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void setUpLoadMoreListener(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    pagination.onNext(pageNumber++);
                    loading = true;
                }
            }
        });


    }


    public void Disposable(){
        compositeDisposable.dispose();
    }
}
