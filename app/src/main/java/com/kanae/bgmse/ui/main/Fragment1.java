package com.kanae.bgmse.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.magnet.MagnetAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment1 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private MagnetAdapter magnetAdapter;
    private List<Magnet> magnetList = new ArrayList<>();
    private ListView magnetListView;


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
        //magnetAdapter = new MagnetAdapter(getActivity(),R.layout.magnet,magnetList);


    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        final TextView textview = view.findViewById(R.id.textView00);
        textview.setText("2233");
        textview.setTextSize(24);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview.setText("33444");
            }
        });


        initView();
        //magnetListView = view.findViewById(R.id.fraglayout1);
        //magnetListView.setAdapter(magnetAdapter);
        return view;

    }

    private void initView(){
        for(int i=0;i<20;i++){
            magnetList.add(new Magnet("label"+i,"conten"+i,Magnet.TYPE_BGM));
        }
        magnetAdapter = new MagnetAdapter(getActivity(),R.layout.magnet,magnetList);

    }

}