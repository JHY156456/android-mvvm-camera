package com.example.mvvmappapplication.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.charlezz.annotation.CharlesIntent;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityLoginBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.BaseActivity;
import com.example.mvvmappapplication.ui.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Lazy;
import timber.log.Timber;

@CharlesIntent
public class LoginActivity extends BaseActivity {
    @Inject
    Lazy<ActivityLoginBinding> binding;
    UserViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        binding.get().setViewModel(viewModel);
        initLiveItems();
    }

    @Override
    public void onRefreshScreen() {

    }

    public void initLiveItems() {
        viewModel.getResponseBodySingleLiveEvent().observe(this,response -> {
            JSONObject jsonObj = null;
            String hi = "";
            try {
                jsonObj = new JSONObject(response.body().string());
                hi = jsonObj.getString("login_STATUS");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            Timber.e("LoginActivity : " +hi);
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        viewModel.getErrorEvent().observe(this,throwable -> {

            Timber.e("throwable : " + throwable.getMessage());
            Toast.makeText(this, "서버에러", Toast.LENGTH_SHORT).show();
            hideSoftKeyboard();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.tvSignIn: {
               // startActivity(Hanwha.intentForRegistrationActivity(this));
                //startActivity(Charles.intentForSecondActivity(this));
                startActivity(new Intent(this,RegistrationActivity.class));
            }
        }

    }
}
