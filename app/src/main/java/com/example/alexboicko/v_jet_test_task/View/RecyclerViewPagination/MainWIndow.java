package com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination;

import android.Manifest;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.User;
import com.example.alexboicko.v_jet_test_task.Model.Pojo.FacebookAPI.UserBundle;
import com.example.alexboicko.v_jet_test_task.R;
import com.example.alexboicko.v_jet_test_task.SharedPreferences.SPreferences;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.Fragments.FavoritesNewsFragment;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.Fragments.NewsFragment;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.Fragments.SearchNewsFragment;
import com.example.alexboicko.v_jet_test_task.View.RecyclerViewPagination.Fragments.SocialFragment;
import com.example.alexboicko.v_jet_test_task.ViewModel.ViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainWIndow extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.navigationButton)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.login_button)
    LoginButton loginFacebook;


    private CallbackManager callbackManager;

    FragmentTransaction fragmentTransaction;

    NewsFragment newsFragment;
    SearchNewsFragment searchNewsFragment;
    SocialFragment socialFragment;
    FavoritesNewsFragment favoritesNewsFragment;

    ViewModel viewModel;

    CompositeDisposable disposable;

    private boolean isInternetConnect = false;




    private Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        ButterKnife.bind(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setVisibility(View.GONE);

        viewModel = new ViewModel(this);

        disposable = new CompositeDisposable();

        callbackManager = CallbackManager.Factory.create();

        newsFragment = new NewsFragment();
        searchNewsFragment = new SearchNewsFragment();
        socialFragment = new SocialFragment();
        favoritesNewsFragment = new FavoritesNewsFragment();

        loginFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getUserProfile(loginResult.getAccessToken());



            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        int permissionNetworkStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionWifiStatus = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE);
        int permissionInternetStatus = ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET);

        if(permissionInternetStatus != PackageManager.PERMISSION_GRANTED
               && permissionWifiStatus != PackageManager.PERMISSION_GRANTED
                && permissionNetworkStatus != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE},1);
        }
        if(isInternetConnect){
            loginFacebook.setVisibility(View.VISIBLE);
        } else {
            if(!SPreferences.isFilePrefEmpty(this)) {
                Toast.makeText(this, "You are working in OfflineMode!", Toast.LENGTH_LONG).show();
                loginFacebook.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                socialFragment.setArguments(SPreferences.loadLastUser(this));
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, socialFragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(this,"You need do a first login with FaceBook",Toast.LENGTH_LONG).show();
            }
        }

    }


    private boolean checkInternetConnect(){

        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return cm.getActiveNetworkInfo() != null && activeNetwork.isConnected() && activeNetwork.isAvailable();
    }
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse  response) {
                        try {




                            bundle.putString(UserBundle.LAST_USER.id,object.getString("id"));
                            bundle.putString(UserBundle.LAST_USER.name,object.getString("name"));
                            bundle.putString(UserBundle.LAST_USER.email,object.getString("email"));
                            bundle.putString(UserBundle.LAST_USER.avatar,new URL(object.getJSONObject("picture").
                                    getJSONObject("data").getString("url")).toString());
                            SPreferences.saveLastUser(MainWIndow.this,object.toString());
                            socialFragment.setArguments(bundle);


                            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentLayout,socialFragment);
                            fragmentTransaction.commit();
                            bottomNavigationView.setVisibility(View.VISIBLE);
                            loginFacebook.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    isInternetConnect  = checkInternetConnect();
                }else {
                    isInternetConnect = checkInternetConnect();
                }

            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode,  data);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()){
            case R.id.news: {
                if(isInternetConnect) {
                    newsFragment.setArguments(bundle);
                } else {
                    newsFragment.setArguments(SPreferences.loadLastUser(this));
                }
                fragmentTransaction.replace(R.id.fragmentLayout, newsFragment);
            }
                break;
            case R.id.search_news:
                if(isInternetConnect) {
                    searchNewsFragment.setArguments(bundle);
                } else {
                    searchNewsFragment.setArguments(SPreferences.loadLastUser(this));
                }
                fragmentTransaction.replace(R.id.fragmentLayout,searchNewsFragment);
                break;
            case R.id.social: {
                if(isInternetConnect) {
                    socialFragment.setArguments(bundle);
                } else {
                    socialFragment.setArguments(SPreferences.loadLastUser(this));
                }
                fragmentTransaction.replace(R.id.fragmentLayout, socialFragment);
            }
                break;
            case R.id.favorites: {
                if(isInternetConnect) {
                    favoritesNewsFragment.setArguments(bundle);
                } else {
                    favoritesNewsFragment.setArguments(SPreferences.loadLastUser(this));
                }
                fragmentTransaction.replace(R.id.fragmentLayout, favoritesNewsFragment);
            }
                break;

        }
        fragmentTransaction.commit();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
