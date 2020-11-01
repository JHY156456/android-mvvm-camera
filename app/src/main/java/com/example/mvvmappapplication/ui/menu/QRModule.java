package com.example.mvvmappapplication.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mvvmappapplication.databinding.FragmentQrBinding;
import com.example.mvvmappapplication.di.ApplicationContext;
import com.example.mvvmappapplication.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class QRModule {
    //데이터 바인딩 클래스 제공
    @Provides
    @FragmentScope
    FragmentQrBinding provideBinding(@ApplicationContext Context context) {
        return FragmentQrBinding.inflate(LayoutInflater.from(context), null, false);
    }

    //Navigation 컴포넌트에서 목적지 간 이동을 담당하는 NavController
    @Provides
    @FragmentScope
    NavController provideNavController(QRFragment fragment){
        return NavHostFragment.findNavController(fragment);
    }

}
