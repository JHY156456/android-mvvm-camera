package com.example.mvvmappapplication.ui.detail;


import com.example.mvvmappapplication.data.entity.Post;

public class PostDetailPostItem extends PostDetailItem {
    private Post post;

    public PostDetailPostItem(Post post) {
        this.post = post;
    }

    @Override
    public Type getType() {
        return Type.POST;
    }

    public String getTitle(){
        return post.getTitle();
    }

    public String getBody(){
        return post.getBody();
    }
}
