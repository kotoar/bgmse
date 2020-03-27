package com.kanae.bgmse.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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


    private void menuPop(View v, final String magLabel) {
        //定义PopupMenu对象
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        //设置PopupMenu的点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Add to Favorate")){
                    musicPool.setFav(magLabel);
                    MagnetSaver magnetSaver = new MagnetSaver();
                    magnetSaver.StackSave(musicPool.getMagnetList());
                    refreshView();
                    ((MainActivity)getActivity()).refreshFrag2();
                    return true;
                }
                if(item.getTitle().equals("Delete")) {
                    musicPool.deleteRes(magLabel);
                    musicPool.resFavList();
                    refreshView();
                    ((MainActivity) getActivity()).refreshFrag2();
                    Toast.makeText(getActivity(), "Deleted" + magLabel, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            }
        });
        //显示菜单
        popupMenu.show();
    }



    private void refreshView(){
        linearLayout1.removeAllViews();
        initView();
    }

    private void addView(Magnet magnet){
        MagnetView child = new MagnetView(getActivity(), magnet);
        child.setData();
        final int childseid = child.getMusicFun();
        final String childLabel = child.getLabel();

        registerForContextMenu(child);

        child.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                menuPop(v, childLabel);
                return true;
            }
        });

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPool.play_se(childLabel);
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