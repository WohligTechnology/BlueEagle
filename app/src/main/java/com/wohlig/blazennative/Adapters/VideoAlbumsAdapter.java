package com.wohlig.blazennative.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.POJOs.VideoAlbumsPojo;
import com.wohlig.blazennative.R;

import java.util.List;

/**
 * Created by Jay on 07-03-2016.
 */
public class VideoAlbumsAdapter extends RecyclerView.Adapter<VideoAlbumsAdapter.CustomViewHolder> {
    private List<VideoAlbumsPojo> videoAlbumsPojoList;
    private int lastPosition = -1;

    public VideoAlbumsAdapter(List<VideoAlbumsPojo> videoAlbumsPojoList) {
        this.videoAlbumsPojoList = videoAlbumsPojoList;
    }

    @Override
    public int getItemCount() {
        return videoAlbumsPojoList.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final VideoAlbumsPojo vap = videoAlbumsPojoList.get(i);
        customViewHolder.llVideoAlbumCard.setTag(vap.getId());

        if (!vap.getImageUrl().isEmpty()) {
            Picasso.with(vap.getContext())
                    .load(vap.getImageUrl())
                    .into(customViewHolder.ivImage);
        }
        customViewHolder.tvTitle.setText(vap.getTitle());
        customViewHolder.tvDesc.setText(vap.getSubTitle());

        customViewHolder.llVideoAlbumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(vap.getContext(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        /*Animation animation = AnimationUtils.loadAnimation(iap.getContext(), (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        customViewHolder.itemView.startAnimation(animation);
        lastPosition = i;*/
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_video_single, viewGroup, false);

        return new CustomViewHolder(itemView);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout llVideoAlbumCard;
        protected ImageView ivImage;
        protected TextView tvTitle, tvDesc;

        public CustomViewHolder(View v) {
            super(v);
            llVideoAlbumCard = (LinearLayout) v.findViewById(R.id.llVideoAlbumCard);
            ivImage = (ImageView) v.findViewById(R.id.ivImage);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvDesc = (TextView) v.findViewById(R.id.tvDesc);
        }
    }
}