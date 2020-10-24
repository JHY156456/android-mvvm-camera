package com.example.mvvmappapplication.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.databinding.FragmentHomeMenuBinding;
import com.example.mvvmappapplication.di.ApplicationContext;
import com.example.mvvmappapplication.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeMenuModule {
    //데이터 바인딩 클래스 제공
    @Provides
    @FragmentScope
    FragmentHomeMenuBinding provideBinding(@ApplicationContext Context context) {
        return FragmentHomeMenuBinding.inflate(LayoutInflater.from(context), null, false);
    }

    //Navigation 컴포넌트에서 목적지 간 이동을 담당하는 NavController
    @Provides
    @FragmentScope
    NavController provideNavController(HomeMenuFragment fragment){
        return NavHostFragment.findNavController(fragment);
    }
    //RecyclerView용 레이아웃 매니저
    @Provides
    @FragmentScope
    LinearLayoutManager provideLinearLayoutManager(@ApplicationContext Context context) {
        return new LinearLayoutManager(context) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
    }
}
