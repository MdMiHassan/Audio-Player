package com.example.audioplayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Save_Que {
    public static void writeMusicQueueOnPref(Context context, ArrayList<String> fileNames){
        Gson gson=new Gson();
        SharedPreferences preferences=context.getSharedPreferences("music_queue",context.MODE_PRIVATE);
        SharedPreferences.Editor se=preferences.edit();
        se.putString("song_queue",gson.toJson(fileNames));
        se.apply();
    }
   public  static ArrayList<String> getMusicQueueFromPref(Context context){
       SharedPreferences preferences=context.getSharedPreferences("music_queue",context.MODE_PRIVATE);
       String jsonstring=preferences.getString("song_queue","");
       Gson gson=new Gson();
       Type type=new TypeToken<ArrayList<String>>(){}.getType();
       ArrayList<String> list=gson.fromJson(jsonstring,type);
       return list;
   }

}
