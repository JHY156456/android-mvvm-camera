package com.example.mvvmappapplication.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.RecyclerAdapterT;
import com.example.mvvmappapplication.custom.RecyclerViewHolderT;
import com.example.mvvmappapplication.databinding.BottomSheetListItemBinding;
import com.example.mvvmappapplication.dto.SelectDialogDto;
import com.example.mvvmappapplication.utils.UIUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 공통 BottomSheetDialog
 *
 * Update by ehjung on 2019-10-02.
 */
public class HSBottomSheetListDialog extends BottomSheetDialog {

    TextView titleTV;
    View titleUnderlineV;
    RecyclerView recyclerView;

    private List<SelectDialogDto> items = new ArrayList<>();
    /* 다중 선택 기능 활성화 여부 */
    private boolean isMultySelected = false;

    ISelectItemListener mListener;

    public HSBottomSheetListDialog(Context context, String[] itemList, int selectedPos) {
        super(context);
        setContentView(R.layout.bottom_sheet_list_default);
        int i=0;
        for(String item : itemList) {
            items.add(new SelectDialogDto(item, item, (i==selectedPos)));
            i++;
        }
        initLayout();
    }

    public HSBottomSheetListDialog(Context context, List<SelectDialogDto> dataList, int selectPosition) {
        super(context);
        setContentView(R.layout.bottom_sheet_list_default);
        this.items = dataList;

        for (int i = 0; i < items.size(); i++) {
            SelectDialogDto data = items.get(i);
            data.setSelected(i == selectPosition);
        }
        initLayout();
    }

    public HSBottomSheetListDialog(Context context, List<SelectDialogDto> items, boolean isMultySelected) {
        super(context);
        setContentView(R.layout.bottom_sheet_list_default);
        this.items = items;
        this.isMultySelected = isMultySelected;
        initLayout();
    }

    private void initLayout() {
        titleTV = findViewById(R.id.titleTV);
        titleUnderlineV = findViewById(R.id.titleUnderlineV);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ListAdapter());
        recyclerView.getAdapter().notifyDataSetChanged();

        if (isMultySelected) {
            View confirmBT = findViewById(R.id.confirmBT);
            confirmBT.setOnClickListener(onClickListener);
            confirmBT.setVisibility(View.VISIBLE);
        }

        if (items.size() > 5) {
            recyclerView.getLayoutParams().height = UIUtil.getPxFromDp(R.dimen.dialog_list_item_height) * 5;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        titleTV.setText(title);
        titleTV.setVisibility(View.VISIBLE);
        titleUnderlineV.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        titleTV.setText(titleId);
        titleTV.setVisibility(View.VISIBLE);
        titleUnderlineV.setVisibility(View.VISIBLE);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (UIUtil.isDoubleClick()) {
                return;
            }
            switch (v.getId()) {
                case R.id.rootLayout:
                    if (mListener == null) {
                        return;
                    }
                    int position = (int) v.getTag();
                    SelectDialogDto item = items.get(position);
                    if (!item.isEnabled()) {
                        return;
                    }
                    if (isMultySelected) {
                        item.setSelected(!item.isSelected());
                        mListener.onSelectedItem(position, item);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    } else {
                        mListener.onSelectedItem(position, item);
                        dismiss();
                    }
                    break;

                case R.id.confirmBT:
                    dismiss();
                    break;
            }
        }
    };

    private class ListAdapter extends RecyclerAdapterT {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListItemViewHolder(parent);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class ListItemViewHolder extends RecyclerViewHolderT<BottomSheetListItemBinding> {

            ListItemViewHolder(ViewGroup parent) {
                super(parent, R.layout.bottom_sheet_list_item);
                getBinding().rootLayout.setOnClickListener(onClickListener);
            }

            @Override
            public void onBindViewHolder(int position) {
                SelectDialogDto item = items.get(position);
                getBinding().value.setText(item.getValue());
                getBinding().value.setChecked(item.isSelected());

                getBinding().rootLayout.setTag(position);
            }
        }
    }

    public void setSelectItemListener(ISelectItemListener listener) {
        mListener = listener;
    }

    public interface ISelectItemListener {
        void onSelectedItem(int position, SelectDialogDto item);
    }
}