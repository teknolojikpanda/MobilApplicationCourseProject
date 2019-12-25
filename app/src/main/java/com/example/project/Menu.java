package com.example.project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    static MusicPlayer player;
    ImageView settings,search,library;
    Button songs,albums,playlists,artists;
    TextView bottomHome;
    ListView sub_playlist;
    String[] maintitle;

    public static reSearch a = new reSearch();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        player = new MusicPlayer(Menu.this,this,false);
        player.changeIntent2();

        settings = findViewById(R.id.settings_icon);
        songs = findViewById(R.id.songsBTN);
        albums = findViewById(R.id.albumsBTN);
        playlists = findViewById(R.id.playlistsBTN);
        artists = findViewById(R.id.artistsBTN);
        search = findViewById(R.id.bottomSearchBTN);
        library = findViewById(R.id.bottomLibraryBTN);
        bottomHome = findViewById(R.id.homeBTNtext);

        bottomHome.setTypeface(bottomHome.getTypeface(), Typeface.BOLD);
        bottomHome.setTextColor(Color.parseColor("#000000"));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,SearchMenu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,LibraryMenu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Settings.class);
                startActivityForResult(intent,1);
            }
        });

        songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        playlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
