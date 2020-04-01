package com.kanae.bgmse.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kanae.bgmse.R;
import com.kanae.bgmse.file.MagnetSaver;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.magnet.MagnetAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.kanae.bgmse.MainActivity.musicPool;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment2 extends Fragment {


    private ListView listview;
    private List<Magnet> magnetList = new ArrayList<>();
    private MagnetAdapter magnetAdapter;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.popupmenu.popup")) {
                Bundle extras = intent.getExtras();
                int position = extras.getInt("ItemPosition");
                menuPop(listview.getChildAt(position-listview.getFirstVisiblePosition()), magnetList.get(position).getLabel());
            }
            if (action.equals("action.mutipleSelect.frag1")){
                magnetAdapter.selectmode = true;
                magnetAdapter.notifyDataSetChanged();
            }
            if (action.equals("action.mutipleCancel.frag1")){
                magnetAdapter.selectmode = false;
                magnetAdapter.notifyDataSetChanged();
            }
            if(action.equals("action.mutipleFavorite")){
                for(Integer selectedPosition:magnetAdapter.getSelectedList()){
                    String magLabel = magnetList.get(selectedPosition).getLabel();
                    musicPool.setFav(magLabel);
                }
                Intent handintent = new Intent();
                handintent.setAction("action.favlist.refresh");
                getActivity().sendBroadcast(handintent);
                MagnetSaver magnetSaver = new MagnetSaver();
                magnetSaver.StackSave(musicPool.getMagnetList());
                magnetAdapter.resetSelect();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.popupmenu.popup");
        filter.addAction("action.mutipleSelect.frag1");
        filter.addAction("action.mutipleCancel.frag1");
        filter.addAction("action.mutipleFavorite");
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
        magnetList = musicPool.getResList();
        magnetAdapter = new MagnetAdapter(getActivity(),magnetList);
        listview.setAdapter(magnetAdapter);
        magnetAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(receiver);
    }

    private void menuPop(View v, final String magLabel) {

        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Add to Favorate")){
                    musicPool.setFav(magLabel);
                    MagnetSaver magnetSaver = new MagnetSaver();
                    magnetSaver.StackSave(musicPool.getMagnetList());
                    Intent intent = new Intent();
                    intent.setAction("action.favlist.refresh");
                    getActivity().sendBroadcast(intent);
                    return true;
                }
                if(item.getTitle().equals("Delete")) {
                    musicPool.deleteRes(magLabel);
                    musicPool.resFavList();
                    Toast.makeText(getActivity(), "Deleted" + magLabel, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            }
        });
        //显示菜单
        popupMenu.show();
    }


}