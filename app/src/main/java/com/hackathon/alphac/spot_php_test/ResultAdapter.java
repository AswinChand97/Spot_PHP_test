package com.hackathon.alphac.spot_php_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Aswin Chandran on 29-03-2018.
 */

public class ResultAdapter extends BaseAdapter {
    ArrayList<String> text;
    ArrayList<ArrayList<String>>  images;
    Context context;
    LayoutInflater inflater;
    public ResultAdapter(Context mainActivity, ArrayList<String> textList, ArrayList<ArrayList<String>> imagesList)
    {
        context = mainActivity;
        text = textList;
        images = imagesList;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder
    {
        TextView tv;
        ImageView iv1,iv2,iv3;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView;
        ViewHolder h = new ViewHolder();
        rowView = inflater. inflate(R.layout.custom_list,viewGroup,false);
        h.tv = (TextView)rowView.findViewById(R.id.text1);
        h.iv1 = (ImageView)rowView.findViewById(R.id.img1);
        h.iv2 = (ImageView)rowView.findViewById(R.id.img2);
        h.iv3 = (ImageView)rowView.findViewById(R.id.img3);
        h.tv.setText(text.get(i));
        ArrayList<String> imagesForPlant = images.get(i);
        Picasso.with(context).load(Config.SUGGESTED_IMAGE_VIEW+imagesForPlant.get(0)).into(h.iv1);
        Picasso.with(context).load(Config.SUGGESTED_IMAGE_VIEW+imagesForPlant.get(1)).into(h.iv2);
        Picasso.with(context).load(Config.SUGGESTED_IMAGE_VIEW+imagesForPlant.get(2)).into(h.iv3);
        return rowView;
    }
}
