package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class CreatePlaylist extends AppCompatActivity {

    DBConnection helper = new DBConnection();
    EditText createPlaylistNameText;
    TextView playlistCreateCancelBTN,playlistCreateBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        createPlaylistNameText = findViewById(R.id.createPlaylistNameText);
        playlistCreateCancelBTN = findViewById(R.id.playlistCreateCancelBTN);
        playlistCreateBTN = findViewById(R.id.playlistCreateBTN);

        playlistCreateCancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
            playlistCreateBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (createPlaylistNameText.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please enter a name for playlist",Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            helper.createPlaylist(createPlaylistNameText.getText().toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(CreatePlaylist.this,LibraryMenu.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
    }
}
