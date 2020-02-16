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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kanae.bgmse.ui.act.ActView;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment3 extends Fragment {

    private ActView seg3Button1;
    private ActView seg3Button2;

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
        seg3Button2 = view.findViewById(R.id.seg3button2);
        seg3Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).callNetFile();
            }
        });

        return view;

    }

}