package com.example.mvvmappapplication.ui;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityMainBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;
import com.example.mvvmappapplication.di.FragmentScope;
import com.example.mvvmappapplication.ui.detail.PostDetailFragment;
import com.example.mvvmappapplication.ui.detail.PostDetailModule;
import com.example.mvvmappapplication.ui.post.PostFragment;
import com.example.mvvmappapplication.ui.post.PostModule;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainModule {

    @Provides
    @ActivityScope
    static ActivityMainBinding provideBinding(MainActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.activity_main);
    }

    @Provides
    @ActivityContext
    static Context provideContext(MainActivity activity) {
        return activity;
    }


    /**
     * - PostModule,PostDetailModule,UserModule(프래그먼트)을
     * 서브 컴포넌트로 정의하기 위해 MainModule에 아래와같이 추가한다.
     * - 87P : 액티비티 또는 서비스를 위한 Dagger 컴포넌트는
     * 애플리케이션 컴포넌트의 서브 컴포넌트로 구성하고(AppComponent)
     * 프래그먼트는 액티비티 컴포넌트의 서브 컴포넌트로 다시 지정한다.
     *
     * @return
     */
    @FragmentScope
    @ContributesAndroidInjector(modules = PostModule.class)
    abstract PostFragment getPostFragment();


    @FragmentScope
    @ContributesAndroidInjector(modules = PostDetailModule.class)
    abstract PostDetailFragment getPostDetailFragment();


}