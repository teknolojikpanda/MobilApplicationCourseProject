package com.example.project;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class reSearch {
    private String[] musicPath = new String[10];
    private String[] musicName = new String[10];
    private String[] artistName = new String[10];
    private Bitmap[] musicImage = new Bitmap[10];
    private MediaPlayer player;
    private String music_nam;
    private String artist_name;
    private Bitmap bitmap;
    private int i;
    private int playingPosition=0;

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public String getMusic_nam() {
        return music_nam;
    }

    public void setMusic_nam(String music_nam) {
        this.music_nam = music_nam;
    }

    public String getArtist_name() {
        return this.artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String[] getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String[] musicPath) {
        this.musicPath = musicPath;
    }

    public String[] getMusicName() {
        return musicName;
    }

    public void setMusicName(String[] musicName) {
        this.musicName = musicName;
    }

    public String[] getArtistName() {
        return artistName;
    }

    public void setArtistName(String[] artistName) {
        this.artistName = artistName;
    }

    public Bitmap[] getMusicImage() {
        return musicImage;
    }

    public void setMusicImage(Bitmap[] musicImage) {
        this.musicImage = musicImage;
    }

    public int getPlayingPosition() {
        return playingPosition;
    }

    public void setPlayingPosition(int playingPosition) {
        this.playingPosition = playingPosition;
    }
}
