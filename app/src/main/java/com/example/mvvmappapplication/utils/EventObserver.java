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
        if (tEvent != null && tEvent.getContentIfNotHandled() != null && onEventChanged != null)
            onEventChanged.onUnhandledContent(tEvent.getContentIfNotHandled());
    }

    public interface OnEventChanged<T> {
        void onUnhandledContent(T data);
    }
}