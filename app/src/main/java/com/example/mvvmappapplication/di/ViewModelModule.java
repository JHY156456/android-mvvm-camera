package com.example.mvvmappapplication.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.HomeViewModel;
import com.example.mvvmappapplication.ui.detail.PostDetailViewModel;
import com.example.mvvmappapplication.ui.menu.CameraViewModel;
import com.example.mvvmappapplication.ui.menu.HomeMenuViewModel;
import com.example.mvvmappapplication.ui.menu.QRViewModel;
import com.example.mvvmappapplication.ui.post.PostViewModel;
import com.example.mvvmappapplication.ui.user.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

//ViewModel과 관련된 내용을 오브젝트 그래프로 관리
@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(AppViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel.class)
    abstract ViewModel bindsPostViewModel(PostViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PostDetailViewModel.class)
    abstract ViewModel bindsPostDetailViewModel(PostDetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindsUserViewModel(UserViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindsHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel.class)
    abstract ViewModel bindsCameraViewModel(CameraViewModel viewModel);
    @Binds
    @IntoMap
    @ViewModelKey(QRViewModel.class)
    abstract ViewModel bindsCameraViewModel(QRViewModel viewModel);
    @Binds
    @IntoMap
    @ViewModelKey(HomeMenuViewModel.class)
    abstract ViewModel bindsHomeMenuViewModel(HomeMenuViewModel viewModel);
}
