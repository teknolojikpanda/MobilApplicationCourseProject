package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    TextView playlistNameTXT, playlistNameTXT2, owner,bottomLibrary;
    ImageView playlistImage_playlist, backBTN,menu,library, search;
    NonScrollListView playlistMusiclistview;
    Button playAll;

    static Context context2;
    static Activity activity;
    MusicPlayer player;

    ArrayList<String> playlist_music_name = new ArrayList<>();
    ArrayList<String> playlist_artist_name = new ArrayList<>();
    ArrayList<Bitmap> playlist_music_image = new ArrayList<>();
    ArrayList<String> playlist_music_path = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        activity = PlaylistActivity.this;
        context2 = getApplicationContext();

        player = new MusicPlayer(PlaylistActivity.this,this,false);
        player.changeIntent2();

        Intent intent = getIntent();
        int playlist_id = intent.getIntExtra("playlist_id",0);
        String playlist_name = intent.getStringExtra("playlist_name");
        String playlist_owner = intent.getStringExtra("owner");
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        playlistNameTXT = findViewById(R.id.playlistNameTXT);
        playlistNameTXT2 = findViewById(R.id.playlistNameTXT2);
        owner = findViewById(R.id.ownerTXT_playlist);
        playlistImage_playlist = findViewById(R.id.playlistImage_playlist);
        backBTN = findViewById(R.id.backBTN_playlist);
        menu = findViewById(R.id.bottomHomeBTN);
        library = findViewById(R.id.bottomLibraryBTN);
        search = findViewById(R.id.bottomSearchBTN);
        bottomLibrary = findViewById(R.id.libraryBTNtext);
        playlistMusiclistview = findViewById(R.id.playlistMusicListview);
        playAll = findViewById(R.id.playAll);

        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.playingPosition = 0;
                player.playAll(playlist_music_path,playlist_music_name,playlist_artist_name,playlist_music_image);
            }
        });

        bottomLibrary.setTypeface(bottomLibrary.getTypeface(), Typeface.BOLD);
        bottomLibrary.setTextColor(Color.parseColor("#000000"));

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.changeIntent1();
                Intent intent = getIntent();
                setResult(2,intent);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaylistActivity.this,Menu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaylistActivity.this,SearchMenu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        playlistNameTXT2.setText(playlist_name);
        playlistNameTXT.setText("Playlist");
        owner.setText(playlist_owner);
        playlistImage_playlist.setImageBitmap(bmp);

        DBConnection helper = new DBConnection();
        try {
            helper.searchPlaylistMusic(playlist_id);
            playlist_music_name = helper.playlist_music_name;
            playlist_artist_name = helper.playlist_artist_name;
            playlist_music_image = helper.playlist_music_image;
            playlist_music_path = helper.playlist_music_path;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        PlaylistListViewAdapter albumAdapter = new PlaylistListViewAdapter(PlaylistActivity.this, playlist_music_name, playlist_artist_name, playlist_music_path, playlist_id);
        playlistMusiclistview.setAdapter(albumAdapter);
        playlistMusiclistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                player.playSong(playlist_music_path.get(position),playlist_music_name.get(position),playlist_artist_name.get(position),playlist_music_image.get(position));
            }
        });
    }
}
