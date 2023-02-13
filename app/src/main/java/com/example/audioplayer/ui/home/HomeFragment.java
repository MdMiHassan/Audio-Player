package com.example.audioplayer.ui.home;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.audioplayer.SongUtil;
import com.example.audioplayer.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.playlistview;
        SongUtil.displaySongs(getActivity(),listView,SongUtil.getAllSongs(Environment.getExternalStorageDirectory()));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}