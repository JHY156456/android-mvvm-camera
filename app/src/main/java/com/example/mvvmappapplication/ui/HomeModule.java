package com.example.mvvmappapplication.ui;

import android.content.Context;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityHomeBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;
import com.example.mvvmappapplication.di.ApplicationContext;
import com.example.mvvmappapplication.di.FragmentScope;
import com.example.mvvmappapplication.ui.detail.PostDetailFragment;
import com.example.mvvmappapplication.ui.detail.PostDetailModule;
import com.example.mvvmappapplication.ui.menu.CameraFragment;
import com.example.mvvmappapplication.ui.menu.CameraModule;
import com.example.mvvmappapplication.ui.menu.HomeMenuFragment;
import com.example.mvvmappapplication.ui.menu.HomeMenuModule;
import com.example.mvvmappapplication.ui.menu.QRFragment;
import com.example.mvvmappapplication.ui.menu.QRModule;
import com.example.mvvmappapplication.ui.user.ProfileFragment;
import com.example.mvvmappapplication.ui.user.ProfileModule;
import com.example.mvvmappapplication.ui.user.UserFragment;
import com.example.mvvmappapplication.ui.user.UserModule;
import com.example.mvvmappapplication.utils.BackPressCloseHandler;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeModule {
    @Provides
    @ActivityScope
    static ActivityHomeBinding provideHomeBinding(HomeActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.activity_home);
    }
    @Provides
    @ActivityContext
    static Context provideContext(HomeActivity activity) {
        return activity;
    }

    @Provides
    @ActivityScope
    @Named("HomeActivity")
    static NavController provideNavController(HomeActivity activity){
        return Navigation.findNavController(activity,R.id.content);
    }

    //RecyclerView용 레이아웃 매니저
    @Provides
    @ActivityScope
    @Named("asdf")
    static LinearLayoutManager provideLinearLayoutManager(@ApplicationContext Context context) {
        return new LinearLayoutManager(context) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
    }

    @Provides
    @ActivityScope
    static BackPressCloseHandler provideBackPressCloseHandler(HomeActivity homeActivity){
        return new BackPressCloseHandler(homeActivity);
    }

    /**
     * 서브컴포넌트 정의
     */
    @FragmentScope
    @ContributesAndroidInjector(modules = CameraModule.class)
    abstract CameraFragment getCameraFragment();
    @FragmentScope
    @ContributesAndroidInjector(modules = QRModule.class)
    abstract QRFragment getQRFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = UserModule.class)
    abstract UserFragment getUserFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = ProfileModule.class)
    abstract ProfileFragment getProfileFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = HomeMenuModule.class)
    abstract HomeMenuFragment getHomeMenuFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = PostDetailModule.class)
    abstract PostDetailFragment getPostDetailFragment();
}
