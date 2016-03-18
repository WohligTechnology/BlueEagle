package com.wohlig.blazennative.Adapters;

/**
 * Created by Jay on 03-03-2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.POJOs.ImageAlbumsPojo;
import com.wohlig.blazennative.R;

import java.util.List;

public class ImageAlbumsAdapter extends RecyclerView.Adapter<ImageAlbumsAdapter.CustomViewHolder> {
    private List<ImageAlbumsPojo> imageAlbumsPojoList;
    private int lastPosition = -1;

    public ImageAlbumsAdapter(List<ImageAlbumsPojo> imageAlbumsPojoList) {
        this.imageAlbumsPojoList = imageAlbumsPojoList;
    }

    @Override
    public int getItemCount() {
        return imageAlbumsPojoList.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final ImageAlbumsPojo iap = imageAlbumsPojoList.get(i);
        //String tag = iap.getId()+"!!!"+iap.getTitle();
        String tag = iap.getId();

        customViewHolder.llImageAlbumCard.setTag(tag);

        if (!iap.getImageUrl().isEmpty()) {
            Picasso.with(iap.getContext())
                    .load(iap.getImageUrl())
                    .into(customViewHolder.ivCover);
        }
        customViewHolder.tvTitle.setText(iap.getTitle());

        /*Animation animation = AnimationUtils.loadAnimation(iap.getContext(), (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        customViewHolder.itemView.startAnimation(animation);
        lastPosition = i;*/
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_photo_single, viewGroup, false);

        return new CustomViewHolder(itemView);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout llImageAlbumCard;
        protected ImageView ivCover;
        protected TextView tvTitle;

        public CustomViewHolder(View v) {
            super(v);
            llImageAlbumCard = (LinearLayout) v.findViewById(R.id.llImageAlbumCard);
            ivCover = (ImageView) v.findViewById(R.id.ivCover);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        }
    }
}
