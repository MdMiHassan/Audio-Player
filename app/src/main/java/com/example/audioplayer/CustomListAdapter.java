package com.example.audioplayer;

import android.app.Activity;
import android.os.Environment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
        private String[] items;
        private Activity activity;
        public CustomListAdapter(Activity activity, String[] items){
            this.activity=activity;
            this.items=items;
        }
        @Override
        public int getCount() {
            return items.length;
        }
        @Override
        public Object getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView=activity.getLayoutInflater().inflate(R.layout.list_item,null);
            TextView songName=myView.findViewById(R.id.textsongname);
            Button more=myView.findViewById(R.id.more_option);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(activity.getApplicationContext(),more, Gravity.LEFT);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.more_op);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.add_q:
                                    ArrayList<String> qulistname= Save_Que.getMusicQueueFromPref(activity.getApplicationContext());
                                    if(qulistname==null) {
                                        qulistname=new ArrayList<>();
                                    }
                                        ArrayList<File> allfile= SongUtil.getAllSongs(Environment.getExternalStorageDirectory());
                                        ArrayList<String> queueFile=new ArrayList<>();
                                        for(File file:allfile){
                                            if(qulistname.contains(file.getName())){
                                                queueFile.add(file.getName());
                                            }
                                        }
                                        queueFile.add(allfile.get(i).getName());
                                        for (String x:qulistname){
                                            System.out.println(x);
                                        }
                                        Save_Que.writeMusicQueueOnPref(activity.getApplicationContext(),queueFile);


                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();

                }
            });
            songName.setSelected(true);
            songName.setText(items[i]);
            return  myView;
        }

}
