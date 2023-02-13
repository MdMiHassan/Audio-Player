package com.example.audioplayer.ui.gallery;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.audioplayer.CustomListAdapter;
import com.example.audioplayer.QueCustomAdapter;
import com.example.audioplayer.Save_Que;
import com.example.audioplayer.SongUtil;
import com.example.audioplayer.databinding.FragmentGalleryBinding;

import java.io.File;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        GalleryViewModel galleryViewModel =
//                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

          final ListView listView= binding.queuePlaylist;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        ArrayList<String> qulistname= Save_Que.getMusicQueueFromPref(getContext());
        System.out.println("jtdhfty");
        if(qulistname!=null){
            for (String x:qulistname){
                System.out.println(x);
            }
            ArrayList<File> allfile= SongUtil.getAllSongs(Environment.getExternalStorageDirectory());
            ArrayList<File> queueFile=new ArrayList<>();
            for(File file:allfile){
                if(qulistname.contains(file.getName())){
                    queueFile.add(file);
                }
            }
            if(allfile.size()>0){
                String[] songName=new String[queueFile.size()];
                for(int i=0;i<queueFile.size();i++){
                    songName[i]=queueFile.get(i).getName().toString().replace(".mp3","").replace(".wav","");
                }
                QueCustomAdapter customListAdapter=new QueCustomAdapter(getActivity(),songName);
                listView.setAdapter(customListAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String songName = (String) listView.getItemAtPosition(i);
                        SongUtil.openPlayer(getActivity(),i, songName, queueFile);
                    }
                });

            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}