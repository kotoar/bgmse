package com.kanae.bgmse.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.magnet.MagnetAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.kanae.bgmse.MainActivity.musicPool;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment1 extends Fragment {

    private ListView listview;
    private List<Magnet> magnetList = new ArrayList<>();
    private MagnetAdapter magnetAdapter;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.favlist.refresh")) {
                initView();
            }

        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.favlist.refresh");
        filter.addAction("action.mutiple.sendfav");
        getActivity().getApplicationContext().registerReceiver(receiver, filter);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        listview = view.findViewById(R.id.listview);

        initView();

        return view;

    }

    private void initView(){
        magnetList = musicPool.getFavList();
        magnetAdapter = new MagnetAdapter(getActivity(),magnetList);
        magnetAdapter.notifyDataSetChanged();
        listview.setAdapter(magnetAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(receiver);
    }
}