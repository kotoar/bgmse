package com.kanae.bgmse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kanae.bgmse.file.FileAddActivity;
import com.kanae.bgmse.file.MagnetSaver;
import com.kanae.bgmse.file.NetFileActivity;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.music.MusicPool;
import com.kanae.bgmse.ui.main.SectionsPagerAdapter;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import static com.kanae.bgmse.file.FileCopy.copyFilesFromRaw;
import static com.kanae.bgmse.file.FileCopy.verifyStoragePermissions;

public class MainActivity extends AppCompatActivity {

    public static MusicPool musicPool;
    ViewPager viewPager;
    TabLayout tablayout;
    SectionsPagerAdapter sectionsPagerAdapter;
    MagnetSaver magnetSaver = new MagnetSaver();

    Toolbar toolbar;
    MenuItem item_fav;
    MenuItem item_delete;
    MenuItem item_mul;

    boolean isextended;

    String main_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshMain");

        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        initViewPager();
        main_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bgmse";
        initFile();
        musicPool = new MusicPool();

        isextended = false;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        item_fav = menu.findItem(R.id.action_favorite);
        item_delete = menu.findItem(R.id.action_delete);
        item_mul = menu.findItem(R.id.action_multiple);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_multiple:
                Toast.makeText(getApplicationContext(),"Mutiple Select",Toast.LENGTH_SHORT).show();


                if(isextended){         //Action Cancel
                    cutToolBar();
                    Intent intent = new Intent();
                    intent.setAction("action.mutipleCancel.frag" + String.valueOf(viewPager.getCurrentItem()));
                    sendBroadcast(intent);
                }
                else{                   //Action MUL
                    extendToolBar();
                    Intent intent = new Intent();
                    intent.setAction("action.mutipleSelect.frag" + String.valueOf(viewPager.getCurrentItem()));
                    sendBroadcast(intent);
                }
                isextended = !isextended;

                return true;

            case R.id.action_favorite:
                Intent intent = new Intent();
                intent.setAction("action.mutipleFavorite");
                sendBroadcast(intent);
                intent.setAction("action.mutipleCancel.frag" + String.valueOf(viewPager.getCurrentItem()));
                sendBroadcast(intent);
                cutToolBar();
                return true;

            case R.id.action_delete:
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"set",Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void extendToolBar() {
        item_fav.setVisible(true);
        item_delete.setVisible(true);
        item_mul.setTitle(R.string.action_mul_extend);
    }

    private void cutToolBar() {
        item_fav.setVisible(false);
        item_delete.setVisible(false);
        item_mul.setTitle(R.string.action_mul_cut);
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshMain"))
            {
                reloadMain();
            }
        }
    };

    public void reloadMain(){
        Intent refintent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(refintent);
        MainActivity.this.finish();
    }


    public void callAddNew(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, FileAddActivity.class);
        startActivity(intent);
    }

    public void callNetFile(){
        Intent intent = new Intent(MainActivity.this, NetFileActivity.class);
        startActivity(intent);
    }

    private void initViewPager(){
        tablayout = (TabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);


        tablayout.setupWithViewPager(viewPager);


        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tablayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(2);
    }

    private void initFile(){
        File file = new File(main_path + "/mainlist.txt");
        File parentpath = new File(main_path);
        if(!parentpath.exists()){
            parentpath.mkdirs();
        }
        if(!file.exists()){
            List<Magnet> list = new ArrayList<Magnet>();
            list.add(new Magnet("angel","chorus_of_angels",0,0));
            list.add(new Magnet("bell","bell ring",0,1));
            list.add(new Magnet("correct","correct answers",0,2));
            list.add(new Magnet("end","end events",0,3));
            list.add(new Magnet("falling","falling",0,4));
            list.add(new Magnet("heartbeats","heartbeats",0,5));
            list.add(new Magnet("siren","alarming",0,6));
            list.add(new Magnet("snoring","snoring",0,7));
            list.add(new Magnet("start","start events",0,8));

            magnetSaver.StackSave(list);

            copyFilesFromRaw(this,R.raw.angel,"angel.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.bell,"bell.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.correct,"correct.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.end,"end.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.falling,"falling.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.heartbeats,"heartbeats.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.siren,"siren.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.snoring,"snoring.mp3",
                    main_path+File.separator+"/se");
            copyFilesFromRaw(this,R.raw.start,"start.mp3",
                    main_path+File.separator+"/se");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}