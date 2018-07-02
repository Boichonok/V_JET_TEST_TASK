package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.alexboicko.v_jet_test_task.CLient.Constants;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.UserBundle;
import com.example.alexboicko.v_jet_test_task.R;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.PaginationRecyclerView.Item.Item;
import com.example.alexboicko.v_jet_test_task.ViewModel.ViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subscribers.DisposableSubscriber;

enum SortType {
    POPULARITY,
    PUBLISH_AT,
    BY_DATE_RANGE,
    BY_SOURCE
}

public class SearchNewsFragment extends Fragment {

   @BindView(R.id.searchTargetEditeText)
    EditText searchTargetEditText;
   @BindView(R.id.filterGroup)
    RadioGroup filterGroup;
   @BindView(R.id.fromDate)
    EditText fromDate;
   @BindView(R.id.toDate)
    EditText toDate;
   @BindView(R.id.sourceInput)
    EditText sourceInput;
   @BindView(R.id.startSearch)
    Button startSearchButton;

   private Context context;
   private Unbinder unbinder;
   private LayoutInflater inflater;
   private ViewModel viewModel;

   private SortType sortType;
   private String searchInput = null;
   private String fromDateInput = null;
   private String toDateInput = null;
   private String sourceNameInput = null;
   private ArrayList<Item> itemList = new ArrayList<>();

   private CompositeDisposable disposable;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_news, container,false);
        this.inflater = inflater;
        unbinder = ButterKnife.bind(this,view);
        viewModel = new ViewModel(context);


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.disposable = new CompositeDisposable();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        disposable.dispose();
        getFragmentManager().beginTransaction().remove(SearchNewsFragment.this);
    }

    @OnCheckedChanged({R.id.sortByPopularity,
            R.id.sortByPublishAt,
            R.id.filtredFromDateToDate,
            R.id.filtredBySource})
    void filterGroup(CompoundButton button, boolean checked){
        if(checked){

            switch (button.getId()){
                case R.id.sortByPopularity:{
                    fromDate.setVisibility(View.GONE);
                    toDate.setVisibility(View.GONE);
                    sourceInput.setVisibility(View.GONE);
                    sortType = SortType.POPULARITY;

                }break;
                case R.id.sortByPublishAt:{
                    fromDate.setVisibility(View.GONE);
                    toDate.setVisibility(View.GONE);
                    sourceInput.setVisibility(View.GONE);
                    sortType = SortType.PUBLISH_AT;

                }break;
                case R.id.filtredFromDateToDate:{
                    fromDate.setVisibility(View.VISIBLE);
                    toDate.setVisibility(View.VISIBLE);
                    sourceInput.setVisibility(View.GONE);
                    sortType = SortType.BY_DATE_RANGE;


                }break;
                case R.id.filtredBySource:{
                    fromDate.setVisibility(View.GONE);
                    toDate.setVisibility(View.GONE);
                    sourceInput.setVisibility(View.VISIBLE);
                    sortType = SortType.BY_SOURCE;

                }break;
            }

        }
    }

    @SuppressLint("ResourceType")
    @OnClick(R.id.startSearch)
    void startSearchNews(){
        searchInput = searchTargetEditText.getText().toString();
        itemList.clear();
        if(searchInput.length() == 0){
            Toast.makeText(context,"Search target is Empty",Toast.LENGTH_LONG).show();
            return;
        }
        switch (sortType){
            case POPULARITY:{
                disposable.add(viewModel.getSearchResultSortingByPopularity(searchInput)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<Item>>() {
                            @Override
                            public void onNext(List<Item> items) {
                                itemList.addAll(items);
                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                showNews();
                            }
                        }));
            }break;
            case PUBLISH_AT:{
                disposable.add(viewModel.getSearchResultSortingByPublishAt(searchInput)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<Item>>() {
                            @Override
                            public void onNext(List<Item> items) {
                                itemList.addAll(items);
                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();

                            }

                            @Override
                            public void onComplete() {
                                showNews();
                            }
                        }));
            }break;
            case BY_SOURCE:{
                sourceNameInput = sourceInput.getText().toString();
                if(searchInput.length() == 0) {
                    Toast.makeText(context,"Source Name Input is Empty!",Toast.LENGTH_LONG).show();
                    return;
                }
                disposable.add(viewModel.getSearchResultFilteredBySource(searchInput,sourceNameInput)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<Item>>() {
                            @Override
                            public void onNext(List<Item> items) {
                                itemList.addAll(items);
                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                showNews();
                            }
                        }));
            }break;
            case BY_DATE_RANGE:{
                fromDateInput = fromDate.getText().toString();
                toDateInput = toDate.getText().toString();
                if (fromDateInput.length() == 0 || toDateInput.length() == 0) {
                    Toast.makeText(context,"From date input or to date input is Empty!",Toast.LENGTH_LONG).show();
                    return;
                }
                disposable.add(viewModel.getSearchResultFilteredByDataRange(searchInput,fromDateInput,toDateInput)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<Item>>() {
                            @Override
                            public void onNext(List<Item> items) {
                                itemList.addAll(items);
                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                showNews();
                            }
                        }));
            }break;
        }
        Log.d("MY_TAG","\nSearchInput: " + searchInput +
        "\nSourceInput: " + sourceNameInput +
        "\nfromDate: " + fromDateInput +
        "\ntoDate: " + toDateInput +
        "\nTypeSort: " + sortType.toString() +
        "\nItemsList: " + itemList.size());

    }

    private void showNews(){
        Log.d("MY_TAG","Search News: " + searchInput +
                "\nSource: " + sourceNameInput +
                "\nfrom Date: " + fromDateInput + " to Date: " + toDateInput +
                "\nItemList.size(): " + itemList.size());
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        Bundle bundleFromMainWindow = getArguments();
        bundle.putSerializable("itemList",itemList);
        bundle.putString(UserBundle.LAST_USER.id,bundleFromMainWindow.getString(UserBundle.LAST_USER.id));
        newsFragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(SearchNewsFragment.this);
        fragmentTransaction.replace(R.id.fragmentLayout,(android.support.v4.app.Fragment)newsFragment);
        fragmentTransaction.commit();
    }
}
