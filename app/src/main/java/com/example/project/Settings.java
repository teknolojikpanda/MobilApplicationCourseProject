package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class Settings extends AppCompatActivity {

    LinearLayout logoutBTN,viewProfileBTN,deleteBTN;
    TextView usernameText,loggedAs,viewProfileText;
    DBConnection helper = new DBConnection();
    ImageView userImage,backBTN;
    AlertDialog.Builder builder;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        viewProfileText = findViewById(R.id.viewProfileText);
        logoutBTN = findViewById(R.id.logoutBTN);
        usernameText = findViewById(R.id.usernameText);
        loggedAs = findViewById(R.id.loggedAs);
        userImage = findViewById(R.id.userImage);
        backBTN = findViewById(R.id.backBTN);
        viewProfileBTN = findViewById(R.id.viewProfileBTN);
        deleteBTN=findViewById(R.id.deleteBTN);

        builder = new AlertDialog.Builder(Settings.this);

        GetFiles files = new GetFiles();
        String loggedAsStr = loggedAs.getText().toString();
        final RWFile storage = new RWFile();
        String user_mail = storage.readFromFile();
        String username = "";
        String path = "";
        try {
            username = helper.getUserName(user_mail);
            path = helper.getPathFromDB(user_mail);
            Bitmap image = files.getImage(path);
            userImage.setImageBitmap(image);
            usernameText.setText(username);
            loggedAs.setText(loggedAsStr+username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        viewProfileText.setText(user_mail);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*viewProfileBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    usernameText.setTextColor(Color.parseColor("#97000000"));
                    viewProfileText.setTextColor(Color.parseColor("#97383838"));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    usernameText.setTextColor(Color.parseColor("#000000"));
                    viewProfileText.setTextColor(Color.parseColor("#383838"));
                }
                return false;
            }
        });*/

        viewProfileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logoutBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    logoutBTN.setBackgroundColor(Color.parseColor("#CECECE"));}
                else{
                    logoutBTN.setBackgroundColor(Color.parseColor("#00FFFFFF"));}
                return false;
            }
        });
        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,MainActivity.class);
                storage.writeToFile("");
                startActivity(intent);
                finish();
            }
        });

        deleteBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    deleteBTN.setBackgroundColor(Color.parseColor("#CECECE"));}
                else{
                    deleteBTN.setBackgroundColor(Color.parseColor("#00FFFFFF"));}
                return false;
            }
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Do you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String user_mail = storage.readFromFile();
                                String user_name = "";
                                try {
                                    user_name = helper.getUserName(user_mail);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                helper.deleteAccount(user_name);
                                Toast.makeText(getApplicationContext(),helper.sendMessage(),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Settings.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alert = builder.create();

                alert.show();

                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.BLACK);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.BLACK);
            }
        });


    }

}

