package com.example.audioplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audioplayer.databinding.ActivityHomeBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;
public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private ListView listView;
    private String[] items;
    public ArrayList<File> mySongs;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarHome.toolbar);

        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySongs=SongUtil.getAllSongs(Environment.getExternalStorageDirectory());
                startActivity(new Intent(getApplicationContext(),player.class).putExtra("songs",mySongs)
                        .putExtra("songname","xyz").putExtra("pos",0));
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getRuntimePermission();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void getRuntimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //displaySongs();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_LONG).show();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
//    public ArrayList<File> getAllSongs(File file){
//        ArrayList<File> allSong=new ArrayList<>();
//        File []files=file.listFiles();
//        for(File currentFile:files){
//            if(currentFile.isDirectory()&&!currentFile.isHidden()){
//                allSong.addAll(getAllSongs(currentFile));
//            }else{
//                if(currentFile.getName().endsWith(".mp3")||currentFile.getName().endsWith(".wav")){
//                    allSong.add(currentFile);
//                }
//            }
//        }
//        return allSong;
//    }
//    void displaySongs(ListView listView){
//        System.out.println("here");
//        mySongs=getAllSongs(Environment.getExternalStorageDirectory());
//        items=new String[mySongs.size()];
//        for(int i=0;i<mySongs.size();i++){
//            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
//        }
//        Home.CustomAdapter ca=new Home.CustomAdapter();
//        listView.setAdapter(ca);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String songName=(String) listView.getItemAtPosition(i);
//                openPlayer(i,songName,mySongs);
//            }
//        });
//    }
//    class CustomAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//
//            return items.length;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            View myView=getLayoutInflater().inflate(R.layout.list_item,null);
//            TextView textsong=myView.findViewById(R.id.textsongname);
//            textsong.setSelected(true);
//            textsong.setText(items[i]);
//            return  myView;
//        }
//    }
//    public void openPlayer(int position,String songName,ArrayList<File> mySongs){
//        startActivity(new Intent(getApplicationContext(),player.class)
//                .putExtra("songs",mySongs)
//                .putExtra("songname",songName)
//                .putExtra("pos",position));
//    }

}