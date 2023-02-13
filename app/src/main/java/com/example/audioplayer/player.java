package com.example.audioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class player extends AppCompatActivity {
    Button btnplay,btnnext,btnprev,btnff,btnr;
    TextView txtsname,txtstart,txtstop;
    SeekBar seekbar;
    ImageView coverArt;
    String sname;
    public static final String EXTRA_NAME="song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mysongs;
    Thread updateSeekbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setTitle("Now playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnplay=findViewById(R.id.playbtn);
        btnnext=findViewById(R.id.btnnext);
        btnprev=findViewById(R.id.btnprev);
        btnff=findViewById(R.id.btnff);
        btnr=findViewById(R.id.btnfr);
        txtsname=findViewById(R.id.txtplsname);
        txtstart=findViewById(R.id.textsstart);
        txtstop=findViewById(R.id.textsstop);
        seekbar=findViewById(R.id.seekbar);
        coverArt=findViewById(R.id.coverimageview);

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent ci=getIntent();
        Bundle bundle=ci.getExtras();
        mysongs=(ArrayList) bundle.getParcelableArrayList("songs");
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        String songname= ci.getStringExtra("songname");
        position=bundle.getInt("pos",0);
        setCoverArt(mysongs,position,coverArt);
        txtsname.setText(mysongs.get(position).getName().toString());
        Uri uri=Uri.parse(mysongs.get(position).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        updateSeekbar=new Thread(){
            @Override
            public void run() {
                super.run();
                int totalduration=mediaPlayer.getDuration();
                int currentposition=0;
                while(currentposition<totalduration){
                    try {
                        sleep(500);
                        currentposition=mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentposition);
                    }catch (InterruptedException|IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekbar.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        seekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.teal_200), PorterDuff.Mode.MULTIPLY);
        seekbar.getThumb().setColorFilter(getResources().getColor(R.color.teal_200), PorterDuff.Mode.SRC_IN);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        String endtime=getTime(mediaPlayer.getDuration());
        txtstop.setText(endtime);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtstart.setText(getTime(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(this,1000);
            }
        },1000);
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }else {
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=(position+1)%mysongs.size();
                Uri u=Uri.parse(mysongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                txtsname.setText(mysongs.get(position).getName().toString());
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnnext.performClick();
            }
        });
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=(position-1)<0?mysongs.size()-1:position-1;
                Uri u=Uri.parse(mysongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                txtsname.setText(mysongs.get(position).getName().toString());
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
            }
        });
        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getDuration()+10000);
                }
            }
        });
        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getDuration()-10000);
                }
            }
        });

    }
    public String getTime(int duration){
        String time="";
        int min=(duration/1000)/60;
        int sec=(duration/1000)%60;
        time+=min+":";
        if(sec<10){
            time+="0";
        }
        time+=sec;
        return time;
    }
    void setCoverArt(ArrayList<File> songList,int songIndex, ImageView coverart){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songList.get(songIndex).getPath());

        byte [] data = mmr.getEmbeddedPicture();
        //coverart is an Imageview object

        // convert the byte array to a bitmap
        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            coverart.setImageBitmap(bitmap); //associated cover art in bitmap
        }
        else
        {
            coverart.setImageResource(R.drawable.playing_note); //any default cover resourse folder
        }

        coverart.setAdjustViewBounds(true);
       //coverart.setLayoutParams(new LinearLayout.LayoutParams(300, 350));
    }

}