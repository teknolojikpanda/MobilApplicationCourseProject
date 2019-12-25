package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnection{
    String DBUserName="gfsdcvkz_root";
    String DBPassword="20150467@Can";
    String ConnectionURL = "jdbc:mysql://teknolojikpanda.educationhost.cloud:3306/gfsdcvkz_project";
    String text = "";
    public String DBusername;
    Context context;
    ArrayList<String> music_name = new ArrayList<>();
    ArrayList<String> music_info = new ArrayList<>();
    ArrayList<Bitmap> music_image = new ArrayList<>();
    ArrayList<String> music_file = new ArrayList<>();
    ArrayList<String> album_name = new ArrayList<>();
    ArrayList<Bitmap> album_image = new ArrayList<>();
    ArrayList<String> artist_name = new ArrayList<>();
    ArrayList<Bitmap> artist_image = new ArrayList<>();
    ArrayList<Integer> album_year = new ArrayList<>();
    ArrayList<String> artist_name_album = new ArrayList<>();
    ArrayList<String> album_music_name = new ArrayList<>();
    ArrayList<String> album_artist_name = new ArrayList<>();
    ArrayList<String> album_music_path = new ArrayList<>();
    ArrayList<String> music_artist_array = new ArrayList<>();
    ArrayList<String> playlist_name = new ArrayList<>();
    ArrayList<String> playlist_owner = new ArrayList<>();
    ArrayList<Integer> playlist_id = new ArrayList<>();
    ArrayList<Bitmap> playlist_image = new ArrayList<>();
    ArrayList<String> playlist_music_name = new ArrayList<>();
    ArrayList<String> playlist_artist_name = new ArrayList<>();
    ArrayList<Bitmap> playlist_music_image = new ArrayList<>();
    ArrayList<String> playlist_music_path = new ArrayList<>();

    @SuppressLint("NewApi")
    public Connection connection(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
            getMessage("Connected");
        }catch (SQLException e){
            getMessage(e.getMessage());
        }
        catch (ClassNotFoundException e){
            getMessage(e.getMessage());
        }
        return connection;
    }

    public boolean login(String user,String pass){
        boolean status = false;
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
            Statement stmt = conn.createStatement();
            String sql = "SELECT user_email,user_password FROM users";
            stmt.executeQuery(sql);
            ResultSet results = stmt.getResultSet();
            while (results.next()){

                DBusername = results.getString(1);
                String DBpass = results.getString(2);

                if (DBusername.equals(user) && DBpass.equals(pass)){
                    status = true;
                }
            }

            results.close();
            stmt.close();
        }
        catch(Exception e){
            getMessage(e.getMessage());
        }
        return status;
    }

    public boolean register(String regEmail, String regPassword, String regPhoneNum){
        boolean status = false;
        String mail = "";

        try {
            Connection conn = null;
            conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
            Statement stmt = conn.createStatement();
            String sql = "SELECT user_email FROM users WHERE user_email='"+regEmail+"'";
            stmt.executeQuery(sql);
            ResultSet result = stmt.getResultSet();
            while (result.next()){
                mail = result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (mail.equals("")) {
            try {
                Connection conn = null;
                conn = DriverManager.getConnection(ConnectionURL, DBUserName, DBPassword);
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO users (user_email,user_password,user_phone) VALUES (?,?,?)";
                PreparedStatement pStatement = conn.prepareStatement(sql);
                pStatement.setString(1, regEmail);
                pStatement.setString(2, regPassword);
                pStatement.setString(3, regPhoneNum);

                pStatement.executeUpdate();
                pStatement.close();

                sql = "SELECT user_id FROM users WHERE user_email='" + regEmail + "'";
                stmt.executeQuery(sql);
                ResultSet results = stmt.getResultSet();
                int user_id = 0;
                while (results.next()) {
                    user_id = results.getInt(1);
                }
                String user_name = "user" + user_id;
                results.close();
                stmt.close();
                sql = "UPDATE users SET user_name = ? WHERE user_id = ?";
                pStatement = conn.prepareStatement(sql);
                pStatement.setString(1, user_name);
                pStatement.setInt(2, user_id);
                pStatement.executeUpdate();
                getMessage("Registered!");
                status = true;
            } catch (Exception e) {
                getMessage("Not Registered!");
            }
        }
        else {
            getMessage("This E-Mail in use!");
        }
        return status;
    }

    public String getUserName(String user_mail) throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT user_name FROM users WHERE user_email='"+user_mail+"'";
        stmt.executeQuery(sql);
        ResultSet results = stmt.getResultSet();
        String user_name = "";
        while (results.next()){
            user_name = results.getString(1);
        }
        results.close();
        stmt.close();
        return user_name;
    }

    public String getPathFromDB(String user_mail) throws SQLException {
        String path = "";
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT user_photo FROM users WHERE user_email='"+user_mail+"'";
        stmt.executeQuery(sql);
        ResultSet results = stmt.getResultSet();
        while (results.next()){
            path = results.getString(1);
        }
        results.close();
        stmt.close();
        return path;
    }

    public void searchMusic(String attr) throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT music.music_name, album.album_name, artist.artist_name, album.album_photo, music.music_file " +
                     "FROM music " +
                     "JOIN music_album ON music.music_id=music_album.music_id " +
                     "JOIN album ON music_album.album_id=album.album_id " +
                     "JOIN music_artist ON music.music_id=music_artist.music_id " +
                     "JOIN artist ON artist.artist_id=music_artist.artist_id " +
                     "WHERE music.music_name LIKE '%"+attr+"%'";
        stmt.executeQuery(sql);
        ResultSet result = stmt.getResultSet();
        while (result.next()){
            music_name.add(result.getString(1));
            String music_album = result.getString(2);
            String music_artist = result.getString(3);
            String image_path = result.getString(4);
            music_file.add(result.getString(5));
            music_info.add(music_album + " | " + music_artist);
            music_artist_array.add(music_artist);
            GetFiles getFiles = new GetFiles();
            music_image.add(getFiles.getImage(image_path));
        }
    }

    public void searchAlbum(String attr) throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT album.album_name, album.album_photo, album.album_year, artist.artist_name " +
                "FROM album " +
                "JOIN music_album ON album.album_id=music_album.album_id " +
                "JOIN music_artist ON music_album.music_id=music_artist.music_id " +
                "JOIN artist ON artist.artist_id=music_artist.music_id  " +
                "WHERE album_name LIKE '%"+attr+"%'";
        stmt.executeQuery(sql);
        ResultSet result = stmt.getResultSet();
        while (result.next()){
            album_name.add(result.getString(1));
            String image_path = result.getString(2);
            album_year.add(result.getInt(3));
            artist_name_album.add(result.getString(4));
            GetFiles getFiles = new GetFiles();
            album_image.add(getFiles.getImage(image_path));
        }
    }

    public void searchArtist(String attr) throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT artist_name, artist_photo from artist WHERE artist_name LIKE '%"+attr+"%'";
        stmt.executeQuery(sql);
        ResultSet result = stmt.getResultSet();
        while (result.next()){
            artist_name.add(result.getString(1));
            String image_path = result.getString(2);
            GetFiles getFiles = new GetFiles();
            artist_image.add(getFiles.getImage(image_path));
        }
    }

    public void searchAlbumMusic(String attr) throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT music.music_name, artist.artist_name, music.music_file " +
                "FROM music " +
                "JOIN music_album ON music.music_id=music_album.music_id " +
                "JOIN album ON music_album.album_id=album.album_id " +
                "JOIN music_artist ON music.music_id=music_artist.music_id " +
                "JOIN artist ON artist.artist_id=music_artist.artist_id " +
                "WHERE album.album_name='"+attr+"'";
        stmt.executeQuery(sql);
        ResultSet result = stmt.getResultSet();
        while (result.next()){
            album_music_name.add(result.getString(1));
            album_artist_name.add(result.getString(2));
            album_music_path.add(result.getString(3));
        }
    }

    public void searchPlaylist(String attr) throws SQLException{
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT playlist.playlist_name,users.user_name,playlist.playlist_image,playlist.playlist_id " +
                "FROM playlist " +
                "JOIN users ON playlist.owner=users.user_id " +
                "WHERE users.user_name='"+attr+"'";
        stmt.executeQuery(sql);
        ResultSet result = stmt.getResultSet();
        while (result.next()){
            playlist_name.add(result.getString(1));
            playlist_owner.add("by " + result.getString(2));
            String image_path = result.getString(3);
            GetFiles getFiles = new GetFiles();
            playlist_image.add(getFiles.getImage(image_path));
            playlist_id.add(result.getInt(4));
        }
    }

    public void searchPlaylistMusic(int attr) throws SQLException{
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "SELECT music.music_name, artist.artist_name, album.album_photo, music.music_file " +
                "FROM music " +
                "JOIN music_album ON music.music_id=music_album.music_id " +
                "JOIN album ON music_album.album_id=album.album_id " +
                "JOIN music_artist ON music.music_id=music_artist.music_id " +
                "JOIN artist ON artist.artist_id=music_artist.artist_id " +
                "JOIN playlist_music ON music.music_id=playlist_music.music_id " +
                "WHERE playlist_music.playlist_id="+attr;
        stmt.executeQuery(sql);
        ResultSet result = stmt.getResultSet();
        while (result.next()){
            GetFiles getFiles = new GetFiles();
            playlist_music_name.add(result.getString(1));
            playlist_artist_name.add(result.getString(2));
            playlist_music_image.add(getFiles.getImage(result.getString(3)));
            playlist_music_path.add(result.getString(4));
        }
    }

    public void createPlaylist(String playlistName) throws SQLException{
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        final RWFile storage = new RWFile();
        String user_mail = storage.readFromFile();
        String sql = "SELECT user_id FROM users WHERE user_email='"+user_mail+"'";
        stmt.executeQuery(sql);
        ResultSet results = stmt.getResultSet();
        int user_id = 0;
        while (results.next()){
            user_id = results.getInt(1);
        }
        sql = "INSERT INTO playlist (playlist_name, owner) VALUES ('"+playlistName+"',(SELECT user_id FROM users WHERE user_id="+user_id+"))";
        Statement pStatement = conn.createStatement();

        pStatement.executeUpdate(sql);
        pStatement.close();
    }

    public void deletePlaylist(String playlistName) throws SQLException{
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "DELETE FROM playlist WHERE playlist_name='"+playlistName+"'";
        stmt.execute(sql);
    }

    public void addToPlaylist(String music_name, String playlist_name)throws SQLException{
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        String sql = "SELECT music.music_id FROM music JOIN music_album ON music.music_id=music_album.music_id JOIN album ON album.album_id=music_album.album_id WHERE music_name='"+music_name+"'";
        Statement stmt = conn.createStatement();
        stmt.executeQuery(sql);
        ResultSet results = stmt.getResultSet();
        int music_id = 0;
        while (results.next()){
            music_id = results.getInt(1);
        }
        sql = "INSERT INTO playlist_music (music_id,playlist_id) VALUES ((SELECT music_id FROM music WHERE music_id="+music_id+"),(SELECT playlist_id FROM playlist WHERE playlist_name='"+playlist_name+"'))";
        Statement pStatement = conn.createStatement();
        pStatement.executeUpdate(sql);
    }

    public void deleteFromPlaylist(String music_name, int playlist_id) throws SQLException{
        Connection conn = null;
        conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
        Statement stmt = conn.createStatement();
        String sql = "DELETE FROM playlist_music WHERE playlist_id="+playlist_id+" AND music_id=(SELECT music_id FROM music WHERE music_name='"+music_name+"')";
        stmt.execute(sql);
    }

    public boolean deleteAccount(String username){
        boolean status = false;
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(ConnectionURL,DBUserName,DBPassword);
            Statement stmt = conn.createStatement();
            String sql = "SELECT playlist_id FROM playlist WHERE owner=(SELECT user_id FROM users WHERE user_name='"+username+"')";
            stmt.executeQuery(sql);
            ResultSet result = stmt.getResultSet();
            ArrayList<Integer> playlist_id = new ArrayList<>();
            while (result.next()){
                playlist_id.add(result.getInt(1));
            }
            for (int i=0;i<playlist_id.size();i++){
                Statement stmt1 = conn.createStatement();
                String sql1 = "DELETE FROM playlist_music WHERE playlist_id="+playlist_id.get(i);
                stmt1.executeUpdate(sql1);
                Statement stmt2 = conn.createStatement();
                String sql2 = "DELETE FROM playlist WHERE playlist_id="+playlist_id.get(i);
                stmt2.executeUpdate(sql2);
            }
            PreparedStatement st = conn.prepareStatement("DELETE FROM users WHERE user_name='"+username+"'");
            st.executeUpdate();

            getMessage("Deleted!");
            status = true;
        }

        catch(Exception e){
            getMessage("Not Deleted!");
        }
        return status;
    }

    public void getMessage(String message){
        text = message;
    }
    public String sendMessage(){
        return text;
    }
}
