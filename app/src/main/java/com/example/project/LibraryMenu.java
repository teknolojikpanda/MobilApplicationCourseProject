package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibraryMenu extends AppCompatActivity {

    static MusicPlayer player;
    ImageView menu,search;
    TextView bottomLibrary,playlistBTN,albumBTN,artistBTN;
    NonScrollListView playlistListView;
    ScrollView playlist,album,artist;
    LinearLayout createPlaylistBTN;

    static Activity activity;

    ArrayList<String> playlist_name = new ArrayList<>();
    ArrayList<String> playlist_owner = new ArrayList<>();
    ArrayList<Bitmap> playlist_image = new ArrayList<>();
    ArrayList<Integer> playlist_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_menu);

        activity = LibraryMenu.this;

        DBConnection helper = new DBConnection();
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

        player = new MusicPlayer(LibraryMenu.this,this,false);
        player.changeIntent2();

        menu = findViewById(R.id.bottomHomeBTN);
        search = findViewById(R.id.bottomSearchBTN);
        bottomLibrary = findViewById(R.id.libraryBTNtext);
        playlistBTN = findViewById(R.id.playlistBTN);
        albumBTN = findViewById(R.id.albumBTN);
        artistBTN = findViewById(R.id.artistBTN);
        playlistListView = findViewById(R.id.playlistListView);
        playlist = findViewById(R.id.playlists);
        album = findViewById(R.id.albums);
        artist = findViewById(R.id.artists);
        createPlaylistBTN = findViewById(R.id.createPlaylistBTN);

        artistBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistBTN.setText("Playlists");
                albumBTN.setText("Albums");
                String mystring = artistBTN.getText().toString();
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                artistBTN.setText(content);
                playlist.setVisibility(View.GONE);
                artist.setVisibility(View.VISIBLE);
                album.setVisibility(View.GONE);
            }
        });

        albumBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistBTN.setText("Playlists");
                artistBTN.setText("Artists");
                String mystring = albumBTN.getText().toString();
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                albumBTN.setText(content);
                playlist.setVisibility(View.GONE);
                album.setVisibility(View.VISIBLE);
                artist.setVisibility(View.GONE);
            }
        });

        playlistBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumBTN.setText("Albums");
                artistBTN.setText("Artists");
                String mystring = playlistBTN.getText().toString();
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                playlistBTN.setText(content);
                album.setVisibility(View.GONE);
                playlist.setVisibility(View.VISIBLE);
                artist.setVisibility(View.GONE);
            }
        });

        if (playlist.getVisibility() == View.VISIBLE){
            String mystring = playlistBTN.getText().toString();
            SpannableString content = new SpannableString(mystring);
            content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
            playlistBTN.setText(content);
        }

        createPlaylistBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryMenu.this,CreatePlaylist.class);
                startActivity(intent);
                finish();
            }
        });

        createPlaylistBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    createPlaylistBTN.setBackgroundColor(Color.parseColor("#CECECE"));}
                else{
                    createPlaylistBTN.setBackgroundColor(Color.parseColor("#00FFFFFF"));}
                return false;
            }
        });

        if (playlist.getVisibility() == View.VISIBLE){
            String mystring = playlistBTN.getText().toString();
            SpannableString content = new SpannableString(mystring);
            content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
            playlistBTN.setText(content);
        }

        bottomLibrary.setTypeface(bottomLibrary.getTypeface(), Typeface.BOLD);
        bottomLibrary.setTextColor(Color.parseColor("#000000"));

        PlaylistCustomListViewAdapter adapter = new PlaylistCustomListViewAdapter(LibraryMenu.this,playlist_name,playlist_owner,playlist_image);
        playlistListView.setAdapter(adapter);

        playlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LibraryMenu.this,PlaylistActivity.class);
                player.changeIntent1();
                intent.putExtra("playlist_id",playlist_id.get(position));
                intent.putExtra("playlist_name",playlist_name.get(position));
                intent.putExtra("owner",playlist_owner.get(position));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                playlist_image.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image",byteArray);
                startActivityForResult(intent, 1);

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryMenu.this,Menu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryMenu.this,SearchMenu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            player.changeIntent2();
        }
    }
}
