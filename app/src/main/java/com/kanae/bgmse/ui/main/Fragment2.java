package com.kanae.bgmse.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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
                menuPop(listview.getChildAt(position-listview.getFirstVisiblePosition()),
                        magnetList.get(position).getLabel(), position);
            }
            if (action.equals("action.mutipleSelect.frag1")){
                magnetAdapter.selectmode = true;
                magnetAdapter.notifyDataSetChanged();
            }
            if (action.equals("action.mutipleCancel.frag1")){
                magnetAdapter.selectmode = false;
                magnetAdapter.notifyDataSetChanged();
            }
            if(action.equals("action.mutipleFavorite") && magnetAdapter.selectmode){
                for(Integer selectedPosition:magnetAdapter.getSelectedList()){
                    String magLabel = magnetList.get(selectedPosition).getLabel();
                    musicPool.setFav(magLabel);
                }
                refreshFrag1();
                MagnetSaver magnetSaver = new MagnetSaver();
                magnetSaver.StackSave(musicPool.getMagnetList());
                magnetAdapter.resetSelect();
                initView();
            }
            if(action.equals("action.mutiple.delete") && magnetAdapter.selectmode){
                for(Integer selectedPosition:magnetAdapter.getSelectedList()){
                    String magLabel = magnetList.get(selectedPosition).getLabel();
                    musicPool.deleteRes(magLabel);
                }
                refreshFrag1();
                MagnetSaver magnetSaver = new MagnetSaver();
                magnetSaver.StackSave(musicPool.getMagnetList());
                magnetAdapter.resetSelect();
                initView();
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
        filter.addAction("action.mutiple.delete");
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

    private void refreshFrag1(){
        Intent handintent = new Intent();
        handintent.setAction("action.favlist.refresh");
        getActivity().sendBroadcast(handintent);
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

    private void menuPop(View v, final String magLabel, int position) {

        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.addItem){
                    musicPool.reverseFav(magLabel);
                    MagnetSaver magnetSaver = new MagnetSaver();
                    magnetSaver.StackSave(musicPool.getMagnetList());
                    refreshFrag1();
                    return true;
                }
                if(item.getTitle().equals("Delete")) {
                    musicPool.deleteRes(magLabel);
                    musicPool.resFavList();
                    MagnetSaver magnetSaver = new MagnetSaver();
                    magnetSaver.StackSave(musicPool.getMagnetList());
                    Toast.makeText(getActivity(),  magLabel + " Deleted.", Toast.LENGTH_SHORT).show();
                    refreshFrag1();
                    initView();
                    return true;
                }
                return true;
            }
        });

        MenuItem item = popupMenu.getMenu().findItem(R.id.addItem);
        if(musicPool.getIsFav(magLabel)){
            item.setTitle("Cancel Favorite");
        }
        else{
            item.setTitle("Add to Favorite");
        }

        popupMenu.show();
    }


}