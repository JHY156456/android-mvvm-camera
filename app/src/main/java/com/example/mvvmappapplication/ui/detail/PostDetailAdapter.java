package com.example.mvvmappapplication.ui.detail;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.util.ViewBindingHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PostDetailAdapter extends RecyclerView.Adapter<ViewBindingHolder> {

    private final List<PostDetailItem> items = new ArrayList<>();

    @Inject
    public PostDetailAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        switch (PostDetailItem.Type.values()[items.get(position).getType().ordinal()]){
            case USER:
                return R.layout.view_post_detail_user;
            case POST:
                return R.layout.view_post_detail_post;
            case COMMENT:
                return R.layout.view_post_detail_comment;
            default :
                throw new IllegalArgumentException();

        }
    }

    @NonNull
    @Override
    public ViewBindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewBindingHolder(parent.getContext(), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBindingHolder holder, int position) {
        //holder.getBinding().setVariable(BR.item, items.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<PostDetailItem> items) {
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }
}
