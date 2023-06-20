package com.digital.payandserve.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.MyUtil;

import java.util.List;

public class MyCustomPagerAdapter extends PagerAdapter {
    private static int currentPage = 0;
    Context context;
    List<String> images;
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.home_slider_item_view, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        MyUtil.setGlideImage(images.get(position), imageView, context);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}