package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.User;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.UserBundle;
import com.example.alexboicko.v_jet_test_task.R;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Adapter.OnItemClickListener;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Adapter.PaginationRecyclerViewAdapter;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.PaginationRecyclerExpansion;
import com.example.alexboicko.v_jet_test_task.ViewModel.ViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

public class FavoritesNewsFragment extends Fragment {
    @BindView(R.id.paginationRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private LayoutInflater inflater;
    private ViewModel viewModel;
    private Context context;

    PaginationRecyclerViewAdapter paginationRecyclerViewAdapter;
    PaginationRecyclerExpansion paginationRecyclerExpansion;
    LinearLayoutManager linearLayoutManager;
    CompositeDisposable disposable;

    ShareDialog shareDialog;
    CallbackManager callbackManager;

    List<Item> itemsList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.disposable =  new CompositeDisposable();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        disposable.dispose();
        paginationRecyclerExpansion.Disposable();
        getFragmentManager().beginTransaction().remove(FavoritesNewsFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment_layout, container,false);
        this.inflater = inflater;
        unbinder = ButterKnife.bind(this,view);
        viewModel = new ViewModel(context);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        paginationRecyclerViewAdapter = new PaginationRecyclerViewAdapter();
        paginationRecyclerExpansion = new PaginationRecyclerExpansion(paginationRecyclerViewAdapter,linearLayoutManager,progressBar,recyclerView);
        recyclerView.setAdapter(paginationRecyclerViewAdapter);

            Bundle bundle = getArguments();
            String userId = bundle.getString(UserBundle.LAST_USER.id);

            disposable.add(viewModel.getFavoritesNews(userId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<Item>>() {
                        @Override
                        public void onNext(List<Item> items) {
                            Log.d("MY_TAG","items from Favorite: " + items.size());
                            if(items.size()>0) {
                                itemsList.addAll(items);
                                paginationRecyclerExpansion.setFirsPage(itemsList.get(0));
                                paginationRecyclerExpansion.subscribeData(itemsList);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        paginationRecyclerExpansion.setUpLoadMoreListener();

        paginationRecyclerViewAdapter.setOnLongItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, Item item, int position) {
                Log.d("MY_TAG", "On Item Click: " + "Item.Title: " + item.getTitle());
                showChoosenDialog(item, inflater);

            }
        });

        return view;
    }

    private void showChoosenDialog(Item item, LayoutInflater inflater){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.chose_news_dialog);
        dialog.setTitle("Choose action");
        Button addFavorite = dialog.findViewById(R.id.addToFaivorite);
        addFavorite.setVisibility(View.GONE);

        Button shareToFacebook = dialog.findViewById(R.id.shareToFacebook);
        shareToFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShareDialog.canShow(ShareLinkContent.class)){
                    ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(item.getUrl().toString()))
                            .build();
                    shareDialog.show(shareLinkContent);
                    dialog.cancel();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}
