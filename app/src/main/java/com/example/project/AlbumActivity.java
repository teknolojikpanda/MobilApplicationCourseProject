package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity{

    TextView albumNameTXT, albumNameTXT2, artsit;
    ImageView albumImage_album, backBTN,menu,library, search;
    NonScrollListView albumMusiclistview;

    static Context context2;
    static MusicPlayer player;

    ArrayList<String> album_music_name = new ArrayList<>();
    ArrayList<String> album_artist_name = new ArrayList<>();
    ArrayList<String> album_music_path = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        context2 = getApplicationContext();

        player = new MusicPlayer(AlbumActivity.this,this,false);
        player.changeIntent2();

        albumNameTXT = findViewById(R.id.albumNameTXT);
        albumNameTXT2 = findViewById(R.id.albumNameTXT2);
        artsit = findViewById(R.id.artist);
        albumImage_album = findViewById(R.id.albumImage_album);
        backBTN = findViewById(R.id.backBTN_album);
        albumMusiclistview = findViewById(R.id.albumMusicListview);
        menu = findViewById(R.id.bottomHomeBTN);
        library = findViewById(R.id.bottomLibraryBTN);
        search = findViewById(R.id.bottomSearchBTN);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        String album_name = intent.getStringExtra("album_name");
        int album_year = intent.getIntExtra("album_year", 0);
        String artist_name = intent.getStringExtra("artist_name");

        final Bitmap image = SearchMenu.album_image.get(position);

        DBConnection connection = new DBConnection();
        try {
            connection.searchAlbumMusic(album_name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumActivity.this,Menu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumActivity.this,LibraryMenu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.changeIntent1();
                finish();
            }
        });

        album_music_name = connection.album_music_name;
        album_artist_name = connection.album_artist_name;
        album_music_path = connection.album_music_path;

        AlbumListViewAdapter albumAdapter = new AlbumListViewAdapter(AlbumActivity.this, album_music_name, album_artist_name, album_music_path);
        albumMusiclistview.setAdapter(albumAdapter);

        albumMusiclistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                player.playSong(album_music_path.get(position),album_music_name.get(position),album_artist_name.get(position),image);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.changeIntent1();
                Intent intent = getIntent();
                setResult(2,intent);
                finish();
            }
        });

        albumNameTXT.setText(album_name);
        albumNameTXT2.setText(album_name);
        artsit.setText(artist_name + " | " + album_year);
        albumImage_album.setImageBitmap(image);
    }
}
