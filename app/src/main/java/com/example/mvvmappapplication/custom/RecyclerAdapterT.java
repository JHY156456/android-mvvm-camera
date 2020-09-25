package com.example.mvvmappapplication.custom;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.utils.ThemeUtil;


/**
 * RecyclerView.Adapter (공통)
 * <p>
 * Created by ehjung on 2017.11.08.
 */
public abstract class RecyclerAdapterT<VM> extends RecyclerView.Adapter<RecyclerViewHolderT> {

    VM mViewModel;
    protected LayoutInflater mInflater;

    protected RecyclerAdapterT() {
    }

    protected RecyclerAdapterT(Context context, VM viewModel) {
        mViewModel = viewModel;
        mInflater = LayoutInflater.from(context);
    }

    protected VM getViewModel() {
        return mViewModel;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderT holder, int position) {
        if (holder.getThemeType() != App.getThemeType()) {
            holder.setThemeType(App.getThemeType());
            ThemeUtil.updateTheme((ViewGroup) holder.itemView);
        }
        holder.onBindViewHolder(position);
    }

}
