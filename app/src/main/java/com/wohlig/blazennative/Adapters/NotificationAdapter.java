package com.wohlig.blazennative.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.POJOs.NotificationPojo;
import com.wohlig.blazennative.R;

import java.util.List;

/**
 * Created by Jay on 18-03-2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomViewHolder> {
    private List<NotificationPojo> notificationPojoList;
    private int lastPosition = -1;

    public NotificationAdapter(List<NotificationPojo> notificationPojoList) {
        this.notificationPojoList = notificationPojoList;
    }

    @Override
    public int getItemCount() {
        return notificationPojoList.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final NotificationPojo np = notificationPojoList.get(i);

        //title
        if(!np.getTitle().isEmpty() || !np.getTitle().equals("")){
            customViewHolder.tvTitle.setText(np.getTitle());
            customViewHolder.tvTitle.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.tvTitle.setVisibility(View.GONE);
        }

        //text or description
        if(!np.getText().isEmpty() || !np.getText().equals("")){
            customViewHolder.tvDesc.setText(np.getText());
            customViewHolder.tvDesc.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.tvDesc.setVisibility(View.GONE);
        }

        // image
        if(!np.getImage().isEmpty() || !np.getImage().equals("")) {
            Picasso.with(np.getContext())
                    .load(np.getImage())
                    .into(customViewHolder.ivIcon);

            customViewHolder.ivIcon.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.ivIcon.setVisibility(View.GONE);
        }
        String tag = np.getType() + "!!!" + np.getLink();
        customViewHolder.llContent.setTag(tag);


        /*Animation animation = AnimationUtils.loadAnimation(iap.getContext(), (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        customViewHolder.itemView.startAnimation(animation);
        lastPosition = i;*/
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_notification_single, viewGroup, false);

        return new CustomViewHolder(itemView);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout llContent;
        protected ImageView ivIcon;
        protected TextView tvTitle, tvDesc;

        public CustomViewHolder(View v) {
            super(v);
            llContent = (LinearLayout) v.findViewById(R.id.llContent);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvDesc = (TextView) v.findViewById(R.id.tvDesc);
        }
    }
}