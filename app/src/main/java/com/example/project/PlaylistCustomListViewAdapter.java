package com.example.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class PlaylistCustomListViewAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<String> playlistText,ownerText;
    private final ArrayList<Bitmap> playlistImage;
    AlertDialog.Builder builder;
    DBConnection helper = new DBConnection();

    public  PlaylistCustomListViewAdapter(Context context, ArrayList<String> playlistText, ArrayList<String> ownerText, ArrayList<Bitmap> playlistImage){
    //adapterin istediği veriler geliyor.

        this.context = context;
        this.playlistText = playlistText;
        this.ownerText = ownerText;
        this.playlistImage = playlistImage;
    }


    @Override
    public int getCount() {
        return playlistText.size();
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

        ViewHolder viewHolder;
        final View result;

        if (convertView==null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.playlist_listview, parent, false);

            viewHolder.playlistTXT = convertView.findViewById(R.id.playlistTXT);
            viewHolder.ownerTXT = convertView.findViewById(R.id.ownerTXT);
            viewHolder.playlistImage = convertView.findViewById(R.id.playlistImage);
            viewHolder.playlistOptionBTN = convertView.findViewById(R.id.playlistOptionBTN);

            builder = new AlertDialog.Builder(context);

            viewHolder.playlistOptionBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setMessage("Do you want to delete?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        helper.deletePlaylist(playlistText.get(position));
                                        Toast.makeText(context,"DONE!",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(context,LibraryMenu.class);
                                        context.startActivity(intent);
                                        LibraryMenu.activity.finish();
                                    } catch (SQLException e) {
                                        Toast.makeText(context,"FAIL!",Toast.LENGTH_LONG).show();
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
                    //Setting the title manually
                    alert.show();

                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.BLACK);
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.BLACK);
                }
            });

            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.playlistTXT.setText(playlistText.get(position));
        viewHolder.ownerTXT.setText(ownerText.get(position));
        viewHolder.playlistImage.setImageBitmap(playlistImage.get(position));

        return convertView;
    }

    private class ViewHolder {

        TextView playlistTXT,ownerTXT;
        ImageView playlistImage,playlistOptionBTN;
    }
}
