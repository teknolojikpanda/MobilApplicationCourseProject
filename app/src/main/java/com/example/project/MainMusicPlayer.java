package com.example.project;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMusicPlayer extends AppCompatActivity {

    ImageView closeBTNmain,preferencesBTNmain;

    MusicPlayer player;
    android.os.Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_music_player);
        player = new MusicPlayer(MainMusicPlayer.this,this,true);
        player.changeIntent2();

        if (player.mediaPlayer.isLooping()){
            player.loopBTN.setImageResource(R.drawable.ic_replay_green_24dp);
        }

        closeBTNmain = findViewById(R.id.closeBTNmain);
        preferencesBTNmain = findViewById(R.id.preferencesBTNmain);

        closeBTNmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SearchMenu.player != null){SearchMenu.player.updateProgressBar();}
                if (Menu.player != null){Menu.player.updateProgressBar();}
                if (LibraryMenu.player != null){LibraryMenu.player.updateProgressBar();}
                if (AlbumActivity.player != null){AlbumActivity.player.updateProgressBar();}
                finish();
            }
        });
    }
}
