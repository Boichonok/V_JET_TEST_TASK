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
import io.reactivex.subscribers.DisposableSubscriber;

public class NewsFragment extends Fragment {

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

    List<Item> items = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment_layout, container,false);
        this.inflater = inflater;
        unbinder = ButterKnife.bind(this,view);
        viewModel = new ViewModel(context);

        Bundle bundle = getArguments();




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
        if (isFromSearchNewsFragmentList()){
            paginationRecyclerExpansion.setFirsPage(items.get(0));
            paginationRecyclerExpansion.subscribeData(items);
        }


        paginationRecyclerExpansion.setUpLoadMoreListener();

        paginationRecyclerViewAdapter.setOnLongItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, Item item, int position) {
                Log.d("MY_TAG","On Item Click: " + "Item.Title: " + item.getTitle());
                showChoosenDialog(item,inflater,bundle.getString(UserBundle.LAST_USER.id));

            }
        });


        return view;
    }

    private void showChoosenDialog(Item item, LayoutInflater inflater,final String userId){
        Dialog dialog = new Dialog(context);
        dialog.addContentView(inflater.inflate(R.layout.chose_news_dialog,null,false),new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Button addFavorite = dialog.findViewById(R.id.addToFaivorite);
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MAY_TAG", "UserID: " + userId);
                    disposable.add(viewModel.addFavoriteNews(item,userId).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            dialog.cancel();
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    }));

            }
        });

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

    private boolean isFromSearchNewsFragmentList(){
        Bundle bundle = getArguments();


        if (bundle != null && bundle.getSerializable("itemList") != null) {
            List<Item> itemList = (List<Item>) bundle.getSerializable("itemList");
            if(itemList.size() > 0) {
                items.addAll(itemList);
                return true;
            }
        }
            return false;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.disposable =  new CompositeDisposable();
    }

    @Override
    public void onDetach() {
        super.onDetach();

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
        getFragmentManager().beginTransaction().remove(NewsFragment.this);
    }
}
