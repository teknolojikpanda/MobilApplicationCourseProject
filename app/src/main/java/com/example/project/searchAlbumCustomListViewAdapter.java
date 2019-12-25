package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class searchAlbumCustomListViewAdapter extends BaseAdapter {

    Context context;
    private final ArrayList<String> albumText;
    private final ArrayList<Bitmap> images;

    public searchAlbumCustomListViewAdapter(Context context, ArrayList<String> albumText, ArrayList<Bitmap> images){

        this.context = context;
        this.albumText = albumText;
        this.images = images;
    }


    @Override
    public int getCount() {
        return albumText.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView==null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.searchalbum_listview, parent, false);

            viewHolder.txtName = convertView.findViewById(R.id.albumText);
            viewHolder.icon = convertView.findViewById(R.id.albumImage);

            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(albumText.get(position));
        viewHolder.icon.setImageBitmap(images.get(position));

        return convertView;
    }

    private class ViewHolder {

        TextView txtName;
        ImageView icon;
    }
}