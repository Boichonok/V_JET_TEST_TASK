package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexboicko.v_jet_test_task.CLient.Constants;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.User;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.UserBundle;
import com.example.alexboicko.v_jet_test_task.R;
import com.example.alexboicko.v_jet_test_task.ViewModel.ViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

public class SocialFragment extends Fragment {
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.userName)
    TextView userNameView;
    @BindView(R.id.userEmail)
    TextView userEmailView;

    Unbinder unbinder;
    ViewModel viewModel;

    CompositeDisposable disposable;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        disposable = new CompositeDisposable();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_layout, container,false);
        unbinder = ButterKnife.bind(this,view);
        viewModel = new ViewModel(context);
        if(getArguments() != null){
            Bundle bundle = getArguments();
            String userId = bundle.getString(UserBundle.LAST_USER.id);
            String userName = bundle.getString(UserBundle.LAST_USER.name);
            String userEmail = bundle.getString(UserBundle.LAST_USER.email);
            String userAvatar = bundle.getString(UserBundle.LAST_USER.avatar);

            try {
                disposable.add(viewModel.setUser(userId,userName,userEmail,new URL(userAvatar)).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        userNameView.setText(userName);
                        userEmailView.setText(userEmail);
                        Picasso.with(context)
                                .load(userAvatar)
                                .placeholder(R.drawable.erroe_load_image)
                                .error(R.drawable.erroe_load_image)
                                .into(avatar);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {

        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        disposable.dispose();
    }
}
