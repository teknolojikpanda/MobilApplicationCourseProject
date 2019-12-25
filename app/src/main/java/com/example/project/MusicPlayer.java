package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MusicPlayer {

    MediaPlayer mediaPlayer = new MediaPlayer();
    android.os.Handler mHandler = new Handler();
    String music_path,music_name,artist_name;
    String musicURL;
    FloatingActionButton playPauseBTN,previousMusicBTN,playPauseBTNmain,nextMusicBTN,loopBTN,shufleBTN;
    SeekBar seekBar,seekBarMain;
    TextView musicNameTXTmini, artistNameTXTmini,musicNameTXTmain,artistNameTXTmain,timeText;
    ImageView miniCloseBTN,musicImageMain;
    LinearLayout mini_MediaPlayer,minitoMain;
    private Context context;
    Activity musicPlayer;
    Bitmap music_image;
    int i = 0;
    int playingPosition=0;
    boolean mode;//mode = false (mini)*
                 //mode = true  (main)


    public MusicPlayer(final Context context, final Activity musicPlayer, boolean mode){
        this.context = context;
        this.musicPlayer = musicPlayer;
        this.mode = mode;
        playPauseBTN = musicPlayer.findViewById(R.id.playPauseBTN);
        miniCloseBTN = musicPlayer.findViewById(R.id.miniCloseBTN);
        seekBar = musicPlayer.findViewById(R.id.seekBar);
        musicNameTXTmini = musicPlayer.findViewById(R.id.musicNameTXTmini);
        artistNameTXTmini = musicPlayer.findViewById(R.id.artistNameTXTmini);
        mini_MediaPlayer = musicPlayer.findViewById(R.id.mini_MediaPlayer_include);
        minitoMain = musicPlayer.findViewById(R.id.minitoMain);

        musicImageMain = musicPlayer.findViewById(R.id.musicImageMain);
        musicNameTXTmain = musicPlayer.findViewById(R.id.musicNameTXTmain);
        artistNameTXTmain = musicPlayer.findViewById(R.id.artistNameTXTmain);
        timeText = musicPlayer.findViewById(R.id.timeText);
        previousMusicBTN = musicPlayer.findViewById(R.id.previousMusicBTN);
        playPauseBTNmain = musicPlayer.findViewById(R.id.playPauseBTNmain);
        nextMusicBTN = musicPlayer.findViewById(R.id.nextMusicBTN);
        loopBTN = musicPlayer.findViewById(R.id.loopBTN);
        shufleBTN = musicPlayer.findViewById(R.id.shufleBTN);
        seekBarMain = musicPlayer.findViewById(R.id.seekBarMain);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        if (mode == false) {
            minitoMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainMusicPlayer.class);
                    changeIntent1();
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    musicPlayer.startActivityForResult(intent, 1);
                }
            });

            playPauseBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying() == true) {
                        playPauseBTN.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        mediaPlayer.pause();
                    } else if (mediaPlayer.isPlaying() == false) {
                        mediaPlayer.start();
                        updateProgressBar();
                        playPauseBTN.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                }
            });

            miniCloseBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = 0;
                    mediaPlayer.reset();
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    mini_MediaPlayer.setVisibility(View.GONE);
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    // forward or backward to certain seconds
                    mediaPlayer.seekTo(seekBar.getProgress());

                    // update timer progress again
                    updateProgressBar();
                }
            });
        }

        if (mode == true){
            shufleBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            loopBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isLooping() == true){
                        mediaPlayer.setLooping(false);
                        loopBTN.setImageResource(R.drawable.ic_replay_black_24dp);
                    }
                    else if (mediaPlayer.isLooping() == false){
                        mediaPlayer.setLooping(true);
                        loopBTN.setImageResource(R.drawable.ic_replay_green_24dp);
                    }
                }
            });

            seekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mMainSeekBarUpdate);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mMainSeekBarUpdate);
                    // forward or backward to certain seconds
                    mediaPlayer.seekTo(seekBarMain.getProgress());

                    // update timer progress again
                    updateMainProgressBar();
                }
            });

            playPauseBTNmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying() == true) {
                        playPauseBTNmain.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        mHandler.removeCallbacks(mMainSeekBarUpdate);
                        mediaPlayer.pause();
                    } else if (mediaPlayer.isPlaying() == false) {
                        mediaPlayer.start();
                        updateMainProgressBar();
                        playPauseBTNmain.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                }
            });
        }
    }

    public void playSong(String music_path, String music_name, String artist_name, Bitmap music_image){
        this.music_path = music_path;
        this.music_name = music_name;
        this.artist_name = artist_name;
        this.music_image = music_image;
        try {
            if (i > 0){
                mHandler.removeCallbacks(mUpdateTimeTask);
                mediaPlayer.reset();
                mediaPlayer = null;
                seekBar.setProgress(0);
                playPauseBTN.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_black_24dp));
            }
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            musicURL = "http://teknolojikpanda.educationhost.cloud" + music_path;
            musicNameTXTmini.setText(music_name);
            artistNameTXTmini.setText(" | " + artist_name);
            if (mini_MediaPlayer != null){
                mini_MediaPlayer.setVisibility(View.VISIBLE);
            }
            mediaPlayer.setDataSource(musicURL);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
            playPauseBTN.setImageResource(R.drawable.ic_pause_black_24dp);
            seekBar.setMax(mediaPlayer.getDuration());
            updateProgressBar();
            i++;
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    ArrayList<String> music_path1 = new ArrayList<>();
    ArrayList<String> music_name1 = new ArrayList<>();
    ArrayList<String> artist_name1 = new ArrayList<>();
    ArrayList<Bitmap> music_image1 = new ArrayList<>();

    String[] musicPath = new String[10];
    String[] musicName = new String[10];
    String[] artistName = new String[10];
    Bitmap[] musicImage = new Bitmap[10];

    public void playAll(ArrayList<String> music_path, ArrayList<String> music_name, ArrayList<String> artist_name, ArrayList<Bitmap> music_mage){
        this.music_path1 = music_path;
        this.music_name1 = music_name;
        this.artist_name1 = artist_name;
        this.music_image1 = music_mage;

        music_path.toArray(musicPath);
        music_name.toArray(musicName);
        artist_name.toArray(artistName);
        music_mage.toArray(musicImage);

        try {
            if (i > 0){
                mHandler.removeCallbacks(mUpdateTimeTask);
                mediaPlayer.reset();
                mediaPlayer = null;
                seekBar.setProgress(0);
                playPauseBTN.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_black_24dp));
            }
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            musicURL = "http://teknolojikpanda.educationhost.cloud" + musicPath[playingPosition];
            musicNameTXTmini.setText(musicName[playingPosition]);
            artistNameTXTmini.setText(" | " + artistName[playingPosition]);
            if (mini_MediaPlayer != null){
                mini_MediaPlayer.setVisibility(View.VISIBLE);
            }
            mediaPlayer.setDataSource(musicURL);
            mediaPlayer.prepare();
            mediaPlayer.start();
            if (mode == false){
                playPauseBTN.setImageResource(R.drawable.ic_pause_black_24dp);
                seekBar.setMax(mediaPlayer.getDuration());
                updateProgressBar();
            }
            i++;
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        if(playingPosition==0){
            if(musicPath.length>1){
                playingPosition++;
            }
        }else if((playingPosition+1)==musicPath.length){
            playingPosition=0;
        }else{
            playingPosition++;
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playAll(music_path1,music_name1,artist_name1,music_image1);
                //do anything else you desire
            }
        });
    }

    public void changeIntent1(){
        Menu.a.setArtist_name(artist_name);
        Menu.a.setI(i);
        Menu.a.setMusic_nam(music_name);
        Menu.a.setPlayer(mediaPlayer);
        Menu.a.setBitmap(music_image);
        Menu.a.setMusicPath(musicPath);
        Menu.a.setMusicName(musicName);
        Menu.a.setArtistName(artistName);
        Menu.a.setMusicImage(musicImage);
        Menu.a.setPlayingPosition(playingPosition);
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    public void changeIntent2(){
        this.artist_name = Menu.a.getArtist_name();
        this.i = Menu.a.getI();
        this.music_name = Menu.a.getMusic_nam();
        this.mediaPlayer = Menu.a.getPlayer();
        this.music_image = Menu.a.getBitmap();
        this.musicPath = Menu.a.getMusicPath();
        this.musicName = Menu.a.getMusicName();
        this.artistName = Menu.a.getArtistName();
        this.musicImage = Menu.a.getMusicImage();
        this.playingPosition = Menu.a.getPlayingPosition();
        if (mode == false) {
            if (i > 0) {
                musicNameTXTmini.setText(music_name);
                artistNameTXTmini.setText(" | " + artist_name);
                mini_MediaPlayer.setVisibility(View.VISIBLE);
                playPauseBTN.setImageResource(R.drawable.ic_pause_black_24dp);
                if (mediaPlayer.isPlaying() == false) {
                    playPauseBTN.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                seekBar.setMax(mediaPlayer.getDuration());
                updateProgressBar();
            } else {
                mini_MediaPlayer.setVisibility(View.GONE);
            }
        }
        if (mode == true){
            musicNameTXTmain.setText(music_name);
            artistNameTXTmain.setText(artist_name);
            musicImageMain.setImageBitmap(music_image);
            playPauseBTNmain.setImageResource(R.drawable.ic_pause_black_24dp);
            if (mediaPlayer.isPlaying() == false) {
                playPauseBTNmain.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }
            seekBarMain.setMax(mediaPlayer.getDuration());
            timeText.setText("1");
            updateMainProgressBar();
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            // Updating progress bar
            if (mediaPlayer != null)
            seekBar.setProgress(mediaPlayer.getCurrentPosition());

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void updateMainProgressBar() {
        mHandler.postDelayed(mMainSeekBarUpdate, 100);
    }

    Runnable mMainSeekBarUpdate = new Runnable() {
        public void run() {
            // Updating progress bar
            seekBarMain.setProgress(mediaPlayer.getCurrentPosition());

            // Update Labels.
            String elapsedTime = createTimeLabel(mediaPlayer.getCurrentPosition());
            timeText.setText(elapsedTime);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };
}