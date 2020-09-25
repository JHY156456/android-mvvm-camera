package com.example.mvvmappapplication.ui.addr;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.RecyclerAdapterT;
import com.example.mvvmappapplication.custom.RecyclerViewHolderT;
import com.example.mvvmappapplication.databinding.ItemUntactSearchAddressBinding;
import com.example.mvvmappapplication.dto.BC1099Q1Dto;


public class UntactSearchAddressAdapter extends RecyclerAdapterT<UntactSearchAddressViewModel> {

    public UntactSearchAddressAdapter(Context context, UntactSearchAddressViewModel viewModel) {
        super(context, viewModel);
    }

    @Override
    public int getItemCount() {
        return getViewModel().getItemCount();
    }

    @NonNull
    @Override
    public RecyclerViewHolderT onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UntactSearchAddressViewHolder(parent);
    }


    /*******************************************************************************
     * RecyclerViewHolderT
     *******************************************************************************/

    private class UntactSearchAddressViewHolder extends RecyclerViewHolderT<ItemUntactSearchAddressBinding> {

        UntactSearchAddressViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_untact_search_address);
            getBinding().clItemUntactSearchAddressBody.setOnClickListener(getViewModel().getOnClickListener());
        }

        @Override
        public void onBindViewHolder(int position) {
            BC1099Q1Dto item = getViewModel().getItem(position);

            getBinding().hsntvItemUntactSearchAddressPostNum.setText(item.ROAD_NM_ZPCD);
            getBinding().hsntvItemUntactSearchAddressRoadAddr.setText(item.ROAD_NM_BSIC_ADDR);
            getBinding().hsntvItemUntactSearchAddressJibunAddr.setText(item.STD_BSIC_ADDR);
            if (position == getItemCount()-1) getBinding().vItemUntactSearchAddressDidvider.setVisibility(View.GONE);

            getBinding().clItemUntactSearchAddressBody.setTag(position);
        }
    }
}