package com.example.mvvmappapplication.ui.githubview;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

  @BindingAdapter({"imageUrl"})
  public static void loadImage(final ImageView imageView, final String imageUrl) {
    // 이미지는 Glide라는 라이브러리를 사용해 데이터를 설정한다
//    Glide.with(imageView.getContext())
//         .load(imageUrl)
//         .asB().centerCrop().into(new BitmapImageViewTarget(imageView) {
//      @Override
//      protected void setResource(Bitmap resource) {
//        // 이미지를 동그랗게 오려낸다
//        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
//        circularBitmapDrawable.setCircular(true);
//        imageView.setImageDrawable(circularBitmapDrawable);
//      }
//    });
  }
}
