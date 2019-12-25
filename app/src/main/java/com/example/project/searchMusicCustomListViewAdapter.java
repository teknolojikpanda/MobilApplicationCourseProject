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

public class searchMusicCustomListViewAdapter extends BaseAdapter {

    Context context;
    private final ArrayList<String> songText,songinfoText;
    private final ArrayList<Bitmap> images;

    public  searchMusicCustomListViewAdapter(Context context, ArrayList<String> songText, ArrayList<String> songinfoText, ArrayList<Bitmap> images){

        this.context = context;
        this.songText = songText;
        this.images = images;
        this.songinfoText = songinfoText;
    }


    @Override
    public int getCount() {
        return songText.size();
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
            convertView = inflater.inflate(R.layout.searchsong_listview, parent, false);

            viewHolder.txtInfo = convertView.findViewById(R.id.songinfoText);
            viewHolder.txtName = convertView.findViewById(R.id.musicText);
            viewHolder.icon = convertView.findViewById(R.id.musicImage);

            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtInfo.setText(songinfoText.get(position));
        viewHolder.txtName.setText(songText.get(position));
        viewHolder.icon.setImageBitmap(images.get(position));

        return convertView;
    }

    private class ViewHolder {

        TextView txtName,txtInfo;
        ImageView icon;
    }
}