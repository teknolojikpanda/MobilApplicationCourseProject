package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AlbumListViewAdapter extends BaseAdapter {

    Context context;
    private final ArrayList<String> musicText;
    private final ArrayList<String> artistText;
    private final ArrayList<String> musicPath;
    AlertDialog.Builder builder;

    DBConnection helper = new DBConnection();

    public AlbumListViewAdapter(Context context, ArrayList<String> musicText, ArrayList<String> artistText, ArrayList<String> musicPath){

        this.context = context;
        this.musicText = musicText;
        this.artistText = artistText;
        this.musicPath = musicPath;
    }


    @Override
    public int getCount() {
        return musicText.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final View result;

        if (convertView==null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.album_listview, parent, false); //Connected listview design.

            viewHolder.musicName = convertView.findViewById(R.id.musicName);
            viewHolder.artistName = convertView.findViewById(R.id.artistName); //Inıtialize elements.
            viewHolder.musicPreferencesBTN = convertView.findViewById(R.id.musicPreferencesBTN);

            viewHolder.musicPreferencesBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  //... send to select playlistactivitty.
                        Intent intent = new Intent(context,SelectPlaylist.class);
                        intent.putExtra("music_name",musicText.get(position)); //selectplaylistactivity send message.
                        context.startActivity(intent);
                }
            });

            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.artistName.setText(artistText.get(position));
        viewHolder.musicName.setText(musicText.get(position));

        return convertView;
    }

    private class ViewHolder { //Call listview elements.

        TextView musicName,artistName;
        ImageView musicPreferencesBTN;
    }
}