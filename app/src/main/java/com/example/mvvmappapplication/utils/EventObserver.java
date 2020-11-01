package com.example.mvvmappapplication.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public class EventObserver<T> implements Observer<Event<T>> {
    private OnEventChanged onEventChanged;

    public EventObserver(OnEventChanged onEventChanged) {
        this.onEventChanged = onEventChanged;
    }

    @Override
    public void onChanged(@Nullable Event<T> tEvent) {
        if (tEvent != null){
            T result = tEvent.getContentIfNotHandled();
            if(result !=null && onEventChanged !=null){
                onEventChanged.onUnhandledContent(result);
            }
        }
    }

    public interface OnEventChanged<T> {
        void onUnhandledContent(T data);
    }
}