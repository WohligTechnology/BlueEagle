package com.wohlig.blazennative.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jay on 07-03-2016.
 */
public class ImageGridAdapter extends BaseAdapter {
    public ArrayList<String> list;
    String listview = "";
    Activity activity;
    TextView noteDesc;
    int imageWidth;

    public ImageGridAdapter(Activity activity, ArrayList<String> list, int imageWidth) {
        super();
        this.activity = activity;
        this.list = list;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        //all the fields in layout specified
        ImageView imageView;
        LinearLayout llSingleImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        String imageLink = null;
        String imageText;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.layout_image_grid, null); //change the name of the layout

            holder.imageView = (ImageView) convertView.findViewById(R.id.ivThumbnail);          //find the different Views
            holder.llSingleImage = (LinearLayout) convertView.findViewById(R.id.llSingleImage);
            holder.llSingleImage.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));

            List<String> imageDetail = Arrays.asList(list.get(position).split("!!!"));

            imageLink = imageDetail.get(0);

            if (!imageLink.equals("") || !imageLink.isEmpty())
                Picasso.with(activity).load(imageLink).into(holder.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
