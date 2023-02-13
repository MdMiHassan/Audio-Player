package com.example.audioplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class SongUtil {
    public static ArrayList<File> getAllSongs(File file){
        ArrayList<File> allSong=new ArrayList<>();
        File []files=file.listFiles();
        for(File currentFile:files){
            if(currentFile.isDirectory()&&!currentFile.isHidden()){
                allSong.addAll(getAllSongs(currentFile));
            }else{
                if(currentFile.getName().endsWith(".mp3")||currentFile.getName().endsWith(".wav")){
                    allSong.add(currentFile);
                }
            }
        }
        return allSong;
    }
    public static void displaySongs(Activity activity, ListView listView, ArrayList<File> songs){
        String[] songName=new String[songs.size()];
        for(int i=0;i<songs.size();i++){
            songName[i]=songs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        CustomListAdapter customListAdapter=new CustomListAdapter(activity,songName);
        listView.setAdapter(customListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = (String) listView.getItemAtPosition(i);
                openPlayer(activity,i, songName, songs);
            }
        });
    }
    public static  void openPlayer(Activity activity, int position, String songName, ArrayList<File> mySongs){
        activity.startActivity(new Intent(activity.getApplicationContext(),player.class).putExtra("songs",mySongs)
                .putExtra("songname",songName).putExtra("pos",position));
    }
}
