package com.wohlig.blazennative.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.POJOs.VideoListPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.Youtube;

import java.util.List;

/**
 * Created by Jay on 11-03-2016.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.CustomViewHolder> {
    private List<VideoListPojo> videoListPojoList;
    private int lastPosition = -1;

    public VideoListAdapter(List<VideoListPojo> videoListPojoList) {
        this.videoListPojoList = videoListPojoList;
    }

    @Override
    public int getItemCount() {
        return videoListPojoList.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final VideoListPojo vlp = videoListPojoList.get(i);

        String youtubeId = vlp.getVideoId();

        if (!youtubeId.isEmpty()) {

            final String playLink = Youtube.getUrl(youtubeId);
            final String thumbnail = Youtube.getThumbnail(youtubeId);

            final Context context = vlp.getContext();
            //customViewHolder.llVideoListCard.setTag(playLink);

            Picasso.with(context)
                    .load(thumbnail)
                    .into(customViewHolder.ivThumbnail);

            customViewHolder.tvTitle.setText(vlp.getTitle());

            customViewHolder.llVideoListCard.setTag(playLink);

            /*customViewHolder.llVideoListCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("webLink", playLink);
                    context.startActivity(intent);
                    //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                }
            });*/
        }

        /*Animation animation = AnimationUtils.loadAnimation(iap.getContext(), (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        customViewHolder.itemView.startAnimation(animation);
        lastPosition = i;*/
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_video_list_single, viewGroup, false);

        return new CustomViewHolder(itemView);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout llVideoListCard;
        protected ImageView ivThumbnail;
        protected TextView tvTitle;

        public CustomViewHolder(View v) {
            super(v);
            llVideoListCard = (LinearLayout) v.findViewById(R.id.llVideoListCard);
            ivThumbnail = (ImageView) v.findViewById(R.id.ivThumbnail);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        }
    }
}