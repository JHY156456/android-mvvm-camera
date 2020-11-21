package com.example.mvvmappapplication.ui.post;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 게시글 목록을 위한 Adapter
 */
public class SlidingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //뷰홀더용 뷰모델 리스트 변수
    private final List<String> items = new ArrayList<>();

    //생성자 인젝션
    @Inject
    public SlidingAdapter(){

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_item_1, parent, false);
        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TextViewHolder) holder).textView.setText(items.get(position));
    }

    //외부로부터 게시글목록을 받아서 UI를 갱신한다.
    public void setItems(List<String> items) {
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    //게시글 목록수
    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_sliding);
        }
        // generate constructor here
    }
}
