package com.wohlig.blazennative.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.POJOs.BlogPojo;
import com.wohlig.blazennative.R;

import java.util.List;

/**
 * Created by Jay on 10-03-2016.
 */
public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.CustomViewHolder> {
    private List<BlogPojo> blogPojoList;
    private int lastPosition = -1;

    public BlogAdapter(List<BlogPojo> blogPojoList) {
        this.blogPojoList = blogPojoList;
    }

    @Override
    public int getItemCount() {
        return blogPojoList.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final BlogPojo bp = blogPojoList.get(i);

        customViewHolder.llBlogCard.setTag(bp.getId());

        if (!bp.getIconUrl().isEmpty()) {
            Picasso.with(bp.getContext())
                    .load(bp.getIconUrl())
                    .into(customViewHolder.ivIcon);
        }

        if(!bp.getTitle().isEmpty())
            customViewHolder.tvTitle.setText(bp.getTitle());

        if(!bp.getTime().isEmpty())
            customViewHolder.tvTime.setText(bp.getTime());

        if(!bp.getDesc().isEmpty())
            customViewHolder.tvDesc.setText(bp.getDesc());

        /*Animation animation = AnimationUtils.loadAnimation(iap.getContext(), (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        customViewHolder.itemView.startAnimation(animation);
        lastPosition = i;*/
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_blog_single, viewGroup, false);

        return new CustomViewHolder(itemView);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout llBlogCard;
        protected ImageView ivIcon;
        protected TextView tvTitle, tvTime, tvDesc;

        public CustomViewHolder(View v) {
            super(v);
            llBlogCard = (LinearLayout) v.findViewById(R.id.llBlogCard);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvTime = (TextView) v.findViewById(R.id.tvTime);
            tvDesc = (TextView) v.findViewById(R.id.tvDesc);
        }
    }
}