package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

public class SearchMenu extends AppCompatActivity {

    static MusicPlayer player;
    DBConnection helper = new DBConnection();
    ImageView menu,library;
    EditText searchTextArea;
    ImageView searchBTN;
    TextView bottomSearch,recentText;
    LinearLayout linearLayout,minitoMain;
    ListView recentListView;
    int i = 1;

    ArrayList<String> artist_name_album = new ArrayList<>();
    ArrayList<Integer> album_year = new ArrayList<>();
    ArrayList<String> album_name = new ArrayList<>();
    static ArrayList<Bitmap> album_image = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        player = new MusicPlayer(SearchMenu.this,this, false);
        player.changeIntent2();

        menu = findViewById(R.id.bottomHomeBTN);
        library = findViewById(R.id.bottomLibraryBTN);
        searchTextArea = findViewById(R.id.searchTextArea);
        searchBTN = findViewById(R.id.searchBTN);
        bottomSearch = findViewById(R.id.searchBTNtext);
        recentText = findViewById(R.id.recentText);
        linearLayout = findViewById(R.id.li);
        recentListView = findViewById(R.id.recentListView);
        minitoMain = findViewById(R.id.minitoMain);

        bottomSearch.setTypeface(bottomSearch.getTypeface(),Typeface.BOLD);
        bottomSearch.setTextColor(Color.parseColor("#000000"));

        if (!searchTextArea.getText().equals("")) {
            searchBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!searchTextArea.getText().toString().equals("")) {
                        String attr = searchTextArea.getText().toString();
                        helper.music_image.clear();
                        helper.music_name.clear();
                        helper.music_info.clear();
                        helper.album_name.clear();
                        helper.album_image.clear();
                        helper.artist_image.clear();
                        helper.artist_name.clear();
                        linearLayout.removeView(recentText);
                        linearLayout.removeView(recentListView);
                        LinearLayout li = new LinearLayout(SearchMenu.this);
                        li.setOrientation(LinearLayout.VERTICAL);
                        if (i > 1) {
                            int a = i - 1;
                            View x = findViewById(a);
                            linearLayout.removeView(x);
                        }
                        li.setId(i);
                        TextView relatedSongsText = new TextView(SearchMenu.this);
                        relatedSongsText.setText("Related songs");
                        relatedSongsText.setTypeface(relatedSongsText.getTypeface(), Typeface.BOLD);
                        relatedSongsText.setTextColor(Color.parseColor("#000000"));
                        relatedSongsText.setTextSize(18);
                        li.addView(relatedSongsText);

                        //Music Search
                        ArrayList<String> music_name = new ArrayList<>();
                        ArrayList<String> music_info = new ArrayList<>();
                        ArrayList<Bitmap> music_image = new ArrayList<>();
                        ArrayList<String> music_file = new ArrayList<>();
                        ArrayList<String> music_artist = new ArrayList<>();
                        try {
                            helper.searchMusic(attr);
                            music_name = helper.music_name;
                            music_info = helper.music_info;
                            music_image = helper.music_image;
                            music_file = helper.music_file;
                            music_artist = helper.music_artist_array;


                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (music_name.size() == 0){
                            TextView noResultText = new TextView(SearchMenu.this);
                            noResultText.setText("No result found");
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(20,20,20,20);
                            noResultText.setLayoutParams(params);
                            li.addView(noResultText);
                        }
                        else {
                            searchMusicCustomListViewAdapter myAdapter = new searchMusicCustomListViewAdapter(SearchMenu.this, music_name, music_info, music_image);
                            NonScrollListView searchListView = new NonScrollListView(SearchMenu.this);
                            searchListView.setAdapter(myAdapter);
                            final ArrayList<String> finalMusic_file = music_file;
                            final ArrayList<String> finalMusic_name = music_name;
                            final ArrayList<String> finalMusic_artist = music_artist;
                            final ArrayList<Bitmap> finalMusic_image = music_image;
                            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    player.playSong(finalMusic_file.get(position),finalMusic_name.get(position),finalMusic_artist.get(position), finalMusic_image.get(position));
                                }
                            });
                            li.addView(searchListView);
                        }

                        TextView relatedAlbumsText = new TextView(SearchMenu.this);
                        relatedAlbumsText.setText("Related albums");
                        relatedAlbumsText.setTypeface(relatedAlbumsText.getTypeface(), Typeface.BOLD);
                        relatedAlbumsText.setTextColor(Color.parseColor("#000000"));
                        relatedAlbumsText.setTextSize(18);
                        li.addView(relatedAlbumsText);

                        try {
                            helper.searchAlbum(attr);
                            album_name = helper.album_name;
                            album_image = helper.album_image;
                            artist_name_album = helper.artist_name_album;
                            album_year = helper.album_year;

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (album_name.size() == 0){
                            TextView noResultText = new TextView(SearchMenu.this);
                            noResultText.setText("No result found");
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(20,20,20,20);
                            noResultText.setLayoutParams(params);
                            li.addView(noResultText);
                        }
                        else {
                            searchAlbumCustomListViewAdapter albumAdapter = new searchAlbumCustomListViewAdapter(SearchMenu.this, album_name, album_image);
                            NonScrollListView albumListView = new NonScrollListView(SearchMenu.this);
                            albumListView.setAdapter(albumAdapter);
                            final ArrayList<String> finalAlbum_name = album_name;
                            final ArrayList<Bitmap> finalAlbum_image = album_image;
                            final ArrayList<Integer> finalAlbum_year = album_year;
                            final ArrayList<String> finalArtist_name = artist_name_album;
                            albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(SearchMenu.this,AlbumActivity.class);
                                    intent.putExtra("album_name", finalAlbum_name.get(position));
                                    intent.putExtra("position", position);
                                    intent.putExtra("album_year", finalAlbum_year.get(position));
                                    intent.putExtra("artist_name", finalArtist_name.get(position));
                                    player.changeIntent1();
                                    startActivityForResult(intent,1);
                                }
                            });
                            li.addView(albumListView);
                        }

                        TextView relatedArtistsText = new TextView(SearchMenu.this);
                        relatedArtistsText.setText("Related artists");
                        relatedArtistsText.setTypeface(relatedArtistsText.getTypeface(), Typeface.BOLD);
                        relatedArtistsText.setTextColor(Color.parseColor("#000000"));
                        relatedArtistsText.setTextSize(18);
                        li.addView(relatedArtistsText);

                        ArrayList<String> artist_name = new ArrayList<>();
                        ArrayList<Bitmap> artist_image = new ArrayList<>();
                        try {
                            helper.searchArtist(attr);
                            artist_name = helper.artist_name;
                            artist_image = helper.artist_image;

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (artist_name.size() == 0){
                            TextView noResultText = new TextView(SearchMenu.this);
                            noResultText.setText("No result found");
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(20,20,20,20);
                            noResultText.setLayoutParams(params);
                            li.addView(noResultText);
                        }
                        else {
                            searchAlbumCustomListViewAdapter artistAdapter = new searchAlbumCustomListViewAdapter(SearchMenu.this, artist_name, artist_image);
                            NonScrollListView artistListView = new NonScrollListView(SearchMenu.this);
                            artistListView.setAdapter(artistAdapter);
                            li.addView(artistListView);
                        }

                        linearLayout.addView(li);
                        i++;
                    }
                }
            });
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMenu.this,Menu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMenu.this,LibraryMenu.class);
                player.changeIntent1();
                startActivityForResult(intent,1);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            player.changeIntent2();
        }
    }
}
