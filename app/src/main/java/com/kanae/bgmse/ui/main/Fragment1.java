package com.kanae.bgmse.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kanae.bgmse.MainActivity;
import com.kanae.bgmse.R;
import com.kanae.bgmse.file.MagnetSaver;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.magnet.MagnetView;

import java.util.HashMap;
import java.util.List;

import static com.kanae.bgmse.MainActivity.musicPool;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment1 extends Fragment {

    private LinearLayout linearLayout1;

    private int touchID;
    private long mLastTime=0;
    private long mCurTime=0;
    private String touchLabel="";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshSEView"))
            {
                refreshView();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touchID = -1;
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.refreshSEView");
        getActivity().getApplicationContext().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        linearLayout1 = view.findViewById(R.id.fraglayout1);
        initView();

        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    musicPool.play_se(touchID);
                    break;
                case 2:
                    musicPool.setFav(touchLabel);
                    MagnetSaver magnetSaver = new MagnetSaver();
                    magnetSaver.StackSave(musicPool.getMagnetList());
                    refreshView();
                    ((MainActivity)getActivity()).refreshFrag2();
                    break;
            }
        }
    };

    private void refreshView(){
        linearLayout1.removeAllViews();
        initView();
    }

    private void addView(Magnet magnet){
        MagnetView child = new MagnetView(getActivity(), magnet);
        child.setData();
        final int childseid = child.getMusicFun();
        final String childLabel = child.getLabel();
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchID = childseid;
                touchLabel = childLabel;
                mLastTime=mCurTime;
                mCurTime= System.currentTimeMillis();
                if(mCurTime-mLastTime<300){
                    mCurTime =0;
                    mLastTime = 0;
                    handler.removeMessages(1);
                    handler.sendEmptyMessage(2);
                }else{
                    handler.sendEmptyMessageDelayed(1, 310);
                }
            }
        });

        if(magnet.getIsfav()==1){
            child.setFavColor();
        }
        linearLayout1.addView(child);
    }

    private void initView(){
        for(Magnet m:musicPool.getMagnetList()){
            addView(m);
        }
    }

}