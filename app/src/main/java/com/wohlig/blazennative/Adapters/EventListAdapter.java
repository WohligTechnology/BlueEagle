package com.wohlig.blazennative.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.POJOs.EventListPojo;
import com.wohlig.blazennative.R;

import java.util.List;

/**
 * Created by Jay on 15-03-2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.CustomViewHolder> {
    private List<EventListPojo> eventListPojoList;
    private int lastPosition = -1;

    public EventListAdapter(List<EventListPojo> eventListPojoList) {
        this.eventListPojoList = eventListPojoList;
    }

    @Override
    public int getItemCount() {
        return eventListPojoList.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final EventListPojo elp = eventListPojoList.get(i);
        customViewHolder.llEventListCard.setTag(elp.getId());


        //URL
        if (!elp.getImageUrl().isEmpty()) {
            customViewHolder.ivThumbnail.setVisibility(View.VISIBLE);
            Picasso.with(elp.getContext())
                    .load(elp.getImageUrl())
                    .into(customViewHolder.ivThumbnail);
        } else {
            customViewHolder.ivThumbnail.setVisibility(View.GONE);
        }


        //Title
        if (!elp.getTitle().isEmpty()) {
            customViewHolder.tvTitle.setText(elp.getTitle());
            customViewHolder.llDate.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.llDate.setVisibility(View.GONE);
        }


        //Date
        if (!elp.getDate().isEmpty()) {
            customViewHolder.tvDate.setText(elp.getDate());
            customViewHolder.llDate.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.llDate.setVisibility(View.GONE);
        }


        //Time
        if (!elp.getTime().isEmpty()) {
            customViewHolder.tvTime.setText(elp.getTime());
            customViewHolder.llTime.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.llTime.setVisibility(View.GONE);
        }


        /*Animation animation = AnimationUtils.loadAnimation(iap.getContext(), (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        customViewHolder.itemView.startAnimation(animation);
        lastPosition = i;*/
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_single_event_list, viewGroup, false);

        return new CustomViewHolder(itemView);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout llEventListCard;
        protected LinearLayout llDate, llTime;
        protected ImageView ivThumbnail;
        protected TextView tvTitle, tvDate, tvTime;

        public CustomViewHolder(View v) {
            super(v);
            llEventListCard = (LinearLayout) v.findViewById(R.id.llEventListCard);
            llDate = (LinearLayout) v.findViewById(R.id.llDate);
            llTime = (LinearLayout) v.findViewById(R.id.llTime);
            ivThumbnail = (ImageView) v.findViewById(R.id.ivThumbnail);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            tvTime = (TextView) v.findViewById(R.id.tvTime);
        }
    }
}