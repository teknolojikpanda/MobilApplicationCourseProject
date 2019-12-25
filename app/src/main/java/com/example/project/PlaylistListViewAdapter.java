package com.example.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlaylistListViewAdapter extends BaseAdapter {

    Context context;
    private final ArrayList<String> musicText;
    private final ArrayList<String> artistText;
    private final ArrayList<String> musicPath;
    private final int playlist_id;
    AlertDialog.Builder builder;

    DBConnection helper = new DBConnection();

    public PlaylistListViewAdapter(Context context, ArrayList<String> musicText, ArrayList<String> artistText, ArrayList<String> musicPath,int playlist_id){

        this.context = context;
        this.musicText = musicText;
        this.artistText = artistText;
        this.musicPath = musicPath;
        this.playlist_id = playlist_id;
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
            convertView = inflater.inflate(R.layout.album_listview, parent, false);

            viewHolder.musicName = convertView.findViewById(R.id.musicName);
            viewHolder.artistName = convertView.findViewById(R.id.artistName);
            viewHolder.musicPreferencesBTN = convertView.findViewById(R.id.musicPreferencesBTN);

            builder = new AlertDialog.Builder(context);

            viewHolder.musicPreferencesBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder = new AlertDialog.Builder(context);

                    viewHolder.musicPreferencesBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.setMessage("Do you want to delete?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                helper.deleteFromPlaylist(musicText.get(position),playlist_id);
                                                Toast.makeText(context,"DELETED",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(context,PlaylistActivity.class);
                                                context.startActivity(intent);
                                                PlaylistActivity.activity.finish();
                                            } catch (SQLException e) {
                                                Toast.makeText(context,"FAILED",Toast.LENGTH_LONG).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                            nbutton.setTextColor(Color.BLACK);
                            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            pbutton.setTextColor(Color.BLACK);
                        }
                    });
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

    private class ViewHolder {

        TextView musicName,artistName;
        ImageView musicPreferencesBTN;
    }
}