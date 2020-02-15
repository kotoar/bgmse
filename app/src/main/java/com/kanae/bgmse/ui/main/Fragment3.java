package com.kanae.bgmse.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.kanae.bgmse.MainActivity;
import com.kanae.bgmse.R;
import com.kanae.bgmse.file.FileAddActivity;
import com.kanae.bgmse.magnet.Magnet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment3 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    //private MagnetAdapter magnetAdapter;
    private List<Magnet> magnetList = new ArrayList<>();

    Button seg3Button1;

    private int getIndex(){
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        return index;
    }


    public static Fragment3 newInstance(int index) {
        Fragment3 fragment = new Fragment3();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        seg3Button1 = view.findViewById(R.id.seg3button1);
        seg3Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).callAddNew();
            }
        });

        return view;

    }



    private void initView(){
        for(int i=0;i<20;i++){
            magnetList.add(new Magnet("label"+i,"conten"+i,Magnet.TYPE_BGM));
        }
        //magnetAdapter = new MagnetAdapter(getActivity(),R.layout.magnet,magnetList);

    }

}