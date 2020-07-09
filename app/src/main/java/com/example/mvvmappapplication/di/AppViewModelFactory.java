package com.example.mvvmappapplication.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

//애플리케이션 스코프로 싱글톤으로 관리한다.

/**
 * Factory 메소드는 인터페이스에서 정의되고, 서브클래스에서 구현된다.
 * 서브클래스에서 구현된 팩토리 메소드는 인스턴스를 생성하는데 사용된다. -> 인스턴스 생성을 서브클래스에게 맡기는것
 */
@Singleton
public class AppViewModelFactory implements ViewModelProvider.Factory {

    //ViewModel 클래스를 키로 갖는 멀티바인딩 된 Map
    private Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public AppViewModelFactory(@NonNull Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //ViewModel 클래스를 키로 하여, ViewModel 객체를 생성하는 Provider를 가져온다.
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            //클래스 키로 못찾았다면 적당한 Provider가 있는지, 다시 Map에서 찾는다.
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                }
            }
        }

        if (creator == null) {
            throw new IllegalArgumentException("Unknown model class " + modelClass);
        }

        try {
            //Dagger의 Provider로 부터 ViewModel 객체 생성 및 반환
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}