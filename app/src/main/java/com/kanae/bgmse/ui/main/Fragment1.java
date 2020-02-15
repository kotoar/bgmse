package com.kanae.bgmse.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.magnet.MagnetView;
import com.kanae.bgmse.music.MusicPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment1 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    //private MagnetAdapter magnetAdapter;
    private List<Magnet> magnetList = new ArrayList<>();
    private HashMap<Integer,Integer> soundID = new HashMap<Integer,Integer>();
    int stack_flag;
    //private ListView magnetListView;
    LinearLayout linearLayout1;

    MusicPool musicPool;

    private int getIndex(){
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        return index;
    }


    /*public static Fragment1 newInstance(int index) {
        Fragment1 fragment = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicPool = new MusicPool();
        magnetList = musicPool.getMagnetList();
        soundID = musicPool.getSoundID();
        stack_flag = 0;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        linearLayout1 = view.findViewById(R.id.fraglayout1);
        //linearLayout1.addView


        ;


        initView();
        //magnetListView = view.findViewById(R.id.fraglayout1);
        //magnetListView.setAdapter(magnetAdapter);
        return view;

    }

    private void addView(Magnet magnet){
        MagnetView child = new MagnetView(getActivity(), magnet,stack_flag);
        child.setData();
        final int childseid = child.getSeid();
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPool.play_se(childseid);
            }
        });
        linearLayout1.addView(child);
        stack_flag += 1;
    }

    private void initView(){
        //addView(new Magnet("111","2222",0));
        //magnetAdapter = new MagnetAdapter(getActivity(),R.layout.magnet,magnetList);
        for(Magnet m:magnetList){
            addView(m);
        }
    }

}