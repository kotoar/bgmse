package com.kanae.bgmse.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.magnet.MagnetView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.kanae.bgmse.MainActivity.musicPool;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment2 extends Fragment {

    LinearLayout linearLayout2;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFavView"))
            {
                refreshView();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.refreshFavView");
        getActivity().getApplicationContext().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        linearLayout2 = view.findViewById(R.id.fraglayout2);
        initView();

        return view;

    }

    private void refreshView(){
        linearLayout2.removeAllViews();
        initView();
    }

    private void initView(){
        musicPool.resFavList();
        for(Magnet m:musicPool.getFavList()){
            addView(m);
        }
    }
    private void addView(Magnet magnet){
        MagnetView child = new MagnetView(getActivity(), magnet);
        child.setData();
        final int childseid = child.getMusicFun();
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPool.play_se(childseid);
            }
        });

        linearLayout2.addView(child);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(receiver);
    }
}