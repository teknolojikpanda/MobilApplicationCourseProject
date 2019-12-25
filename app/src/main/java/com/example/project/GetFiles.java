package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class GetFiles {

    String text = "";
    DBConnection connection = new DBConnection();

    public Bitmap getImage(String path){


        String imageURL = "http://teknolojikpanda.educationhost.cloud"+path;

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return bitmap;
    }

    public void getMessage(String message){
        text = message;
    }
    public String sendMessage(){
        return text;
    }
}
