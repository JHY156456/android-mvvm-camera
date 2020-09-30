package com.example.mvvmappapplication.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mvvmappapplication.databinding.FragmentCameraBinding;
import com.example.mvvmappapplication.di.ApplicationContext;
import com.example.mvvmappapplication.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class CameraModule {
    //데이터 바인딩 클래스 제공
    @Provides
    @FragmentScope
    FragmentCameraBinding provideBinding(@ApplicationContext Context context) {
        return FragmentCameraBinding.inflate(LayoutInflater.from(context), null, false);
    }

    //Navigation 컴포넌트에서 목적지 간 이동을 담당하는 NavController
    @Provides
    @FragmentScope
    NavController provideNavController(CameraFragment fragment){
        return NavHostFragment.findNavController(fragment);
    }

}
