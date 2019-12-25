package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

public class SelectPlaylist extends AppCompatActivity {

    DBConnection helper = new DBConnection();
    NonScrollListView selectPlaylist;
    TextView cancelSelectBTN;

    ArrayList<String> playlist_name = new ArrayList<>();
    ArrayList<String> playlist_owner = new ArrayList<>();
    ArrayList<Bitmap> playlist_image = new ArrayList<>();
    ArrayList<Integer> playlist_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        Intent intent = getIntent(); //Intent yardımıyla
        final String music_name = intent.getStringExtra("music_name"); //Albumactivityden gelen verileri alıyoruz.
        selectPlaylist = findViewById(R.id.selectPlaylist);
        cancelSelectBTN = findViewById(R.id.cancelSelectBTN);

        cancelSelectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RWFile storage = new RWFile();
        String user_mail = storage.readFromFile();

        String user_name = "";
        try {
            user_name = helper.getUserName(user_mail);
            helper.searchPlaylist(user_name);
            playlist_name = helper.playlist_name;
            playlist_owner = helper.playlist_owner;
            playlist_image = helper.playlist_image;
            playlist_id = helper.playlist_id;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PlaylistCustomListViewAdapter adapter = new PlaylistCustomListViewAdapter(SelectPlaylist.this,playlist_name,playlist_owner,playlist_image);
        selectPlaylist.setAdapter(adapter);
        selectPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    helper.addToPlaylist(music_name,playlist_name.get(position));
                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
