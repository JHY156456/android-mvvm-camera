package com.example.mvvmappapplication.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.consts.Const;

/**
 * RecyclerViewHolder (공통)
 *
 * @author ehjung
 * @since 2017.11.08.
 */
public abstract class RecyclerViewHolderT<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private T mBinding;

    private Const.eThemeType mThemeType; // 적용 테마

    /**
     * ViewHolder 생성.
     *
     * @param parent      parent
     * @param layoutResId layoutResId
     */
    public RecyclerViewHolderT(ViewGroup parent, int layoutResId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
        try {
            mBinding = DataBindingUtil.bind(itemView);
        } catch (Exception e) {
        }
        setThemeType(App.getThemeType());
    }

    public RecyclerViewHolderT(View itemView) {
        super(itemView);
        setThemeType(App.getThemeType());
    }

    /**
     * onBindViewHolder.
     *
     * @param position position
     */
    public abstract void onBindViewHolder(int position);

    /**
     * View 에 적용된 테마 타입 반환
     *
     * @return
     */
    public Const.eThemeType getThemeType() {
        return mThemeType;
    }

    /**
     * View 에 적용된 테마 타입 변경
     *
     * @param type
     */
    public void setThemeType(Const.eThemeType type) {
        mThemeType = type;
    }

    public T getBinding() {
        return mBinding;
    }
}
