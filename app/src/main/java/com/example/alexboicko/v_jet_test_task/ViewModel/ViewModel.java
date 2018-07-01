package com.example.alexboicko.v_jet_test_task.ViewModel;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.alexboicko.v_jet_test_task.CLient.IApi;
import com.example.alexboicko.v_jet_test_task.CLient.Constants;
import com.example.alexboicko.v_jet_test_task.CLient.NetworkApplication;
import com.example.alexboicko.v_jet_test_task.Model.DataBase.DataBaseManager;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.User;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.NewsAPI.Article;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.NewsAPI.Everything;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;
import com.example.alexboicko.v_jet_test_task.ViewModel.Adapters.ItemAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModel {






    private IApi api;
    private DataBaseManager dataBaseManager;

    public ViewModel(Context context){
        api = NetworkApplication.getAPI();
        dataBaseManager = DataBaseManager.getInstance(context);
    }

    public Flowable<List<Item>> getSearchResultSortingByPopularity(String searchTarget){

        return Flowable.create(new FlowableOnSubscribe<List<Item>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Item>> e) throws Exception {
                api.getSearchResultSortedBy(searchTarget, Constants.SORT_BY.POPULARITY,Constants.API_KEY).enqueue(new Callback<Everything>() {
                    @Override
                    public void onResponse(Call<Everything> call, Response<Everything> response) {
                        Log.d("MY_TAG","Response Retrofit2: " + response.body().getArticles().get(0).getTitle());
                        List<Item> items = new ArrayList<>();
                        for (Article articleItem: response.body().getArticles()) {
                            Item item = new ItemAdapter(articleItem);
                            Log.d("MY_TAG","Item - title: " + item.getTitle());
                            items.add(item);

                        }
                        if(items.size() == 0){
                            e.onError(new Throwable("No any news"));
                        }
                        e.onNext(items);
                            e.onComplete();
                    }

                    @Override
                    public void onFailure(Call<Everything> call, Throwable t) {
                        e.onError(t);
                    }
                });
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Item>> getSearchResultSortingByPublishAt(String searchTarget){
        return Flowable.create(new FlowableOnSubscribe<List<Item>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Item>> e) throws Exception {
                api.getSearchResultSortedBy(searchTarget,Constants.SORT_BY.PUBLISHED_AT,Constants.API_KEY).enqueue(new Callback<Everything>() {
                    @Override
                    public void onResponse(Call<Everything> call, Response<Everything> response) {
                        List<Item> items = new ArrayList<>();
                        for (Article articleItem : response.body().getArticles()) {
                            Item item = new ItemAdapter(articleItem);
                            items.add(item);
                        }
                        if(items.size() == 0){
                            e.onError(new Throwable("No any news"));
                        }
                        e.onNext(items);
                        e.onComplete();
                    }

                    @Override
                    public void onFailure(Call<Everything> call, Throwable t) {
                        e.onError(t);
                    }
                });
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Item>> getSearchResultFilteredByDataRange(String searchTarget,String fromDate,String toDate) {
        return Flowable.create(new FlowableOnSubscribe<List<Item>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Item>> e) throws Exception {
                api.getSearchResultFilteredByDateRange(searchTarget,fromDate.toString(),toDate.toString(),Constants.API_KEY).enqueue(new Callback<Everything>() {
                    @Override
                    public void onResponse(Call<Everything> call, Response<Everything> response) {
                        List<Item> items = new ArrayList<>();
                        for (Article articleItem : response.body().getArticles()) {
                            Item item = new ItemAdapter(articleItem);
                            items.add(item);
                        }
                        if(items.size() == 0){
                            e.onError(new Throwable("No any news"));
                        }
                        e.onNext(items);
                        e.onComplete();
                    }

                    @Override
                    public void onFailure(Call<Everything> call, Throwable t) {
                        e.onError(t);
                    }
                });
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Item>> getSearchResultFilteredBySource(String searchTarget, String searchInResource){
        return Flowable.create(new FlowableOnSubscribe<List<Item>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Item>> e) throws Exception {
                api.getSearchResultFilteredBySource(searchTarget,searchInResource,Constants.API_KEY).enqueue(new Callback<Everything>() {
                    @Override
                    public void onResponse(Call<Everything> call, Response<Everything> response) {
                        List<Item> items = new ArrayList<>();
                        for (Article articleItem : response.body().getArticles()) {
                            Item item = new ItemAdapter(articleItem);
                            items.add(item);
                        }
                        if(items.size() == 0){
                            e.onError(new Throwable("No any news"));
                        }
                        e.onNext(items);
                        e.onComplete();
                    }

                    @Override
                    public void onFailure(Call<Everything> call, Throwable t) {
                        e.onError(t);
                    }
                });
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Item>> getSearchResult(String searchTarget) {
        return Flowable.create(new FlowableOnSubscribe<List<Item>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Item>> e) throws Exception {
                api.getSearchResults(searchTarget,Constants.LANGUAGE_NEWS.ENGLISH,Constants.API_KEY).enqueue(new Callback<Everything>() {
                    @Override
                    public void onResponse(Call<Everything> call, Response<Everything> response) {
                        List<Item> items = new ArrayList<>();
                        for (Article articleItem : response.body().getArticles()) {
                            Item item = new ItemAdapter(articleItem);
                            items.add(item);
                        }
                        if(items.size() == 0){
                            e.onError(new Throwable("No any news"));
                        }
                        e.onNext(items);
                        e.onComplete();
                    }

                    @Override
                    public void onFailure(Call<Everything> call, Throwable t) {
                        e.onError(t);
                    }
                });
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable setUser(String id, String name, String email, URL picture){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {

                if(dataBaseManager.addRowToUserTable(new User(id,name,email,picture))) {
                    e.onComplete();
                }else {
                    e.onError(new Throwable("User wasn't set"));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public io.reactivex.Observable<User> getUser(String userId){
        return io.reactivex.Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                    e.onNext(dataBaseManager.getUserById(userId));
                    e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addFavoriteNews(Item item, String userId){
       return Completable.create(new CompletableOnSubscribe() {
           @Override
           public void subscribe(CompletableEmitter e) throws Exception {
               if(dataBaseManager.addRowToNewsDataTable(item, userId)){
                   e.onComplete();
               } else {
                   e.onError(new Throwable("News wasn't added"));
               }
           }
       }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public io.reactivex.Observable<List<Item>> getFavoritesNews(String userId){
        return io.reactivex.Observable.create(new ObservableOnSubscribe<List<Item>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Item>> e) throws Exception {
                Log.d("MY_TAG","List item from ViewModel: " + dataBaseManager.getListItemByUserId(userId));
                e.onNext(dataBaseManager.getListItemByUserId(userId));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
