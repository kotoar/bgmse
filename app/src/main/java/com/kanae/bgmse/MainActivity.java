package com.kanae.bgmse;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kanae.bgmse.file.FileAddActivity;
import com.kanae.bgmse.file.MagnetSaver;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.ui.main.SectionsPagerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.kanae.bgmse.file.FileCopy.copyFilesFromRaw;
import static com.kanae.bgmse.file.FileCopy.verifyStoragePermissions;
import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tablayout;
    FloatingActionButton fab;
    SectionsPagerAdapter sectionsPagerAdapter;


    String main_path;

    MagnetSaver magnetSaver = new MagnetSaver();

    //private ActivityRefreshFmInVpBinding binding;

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

    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshMain"))
            {
                Intent refintent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(refintent);
                MainActivity.this.finish();
            }
        }
    };


    public void callAddNew(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, FileAddActivity.class);
        startActivity(intent);
    }

    private void initViewPager(){
        tablayout = (TabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
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
            list.add(new Magnet("angel","chorus_of_angels",0));
            list.add(new Magnet("bell","bell ring",0));
            list.add(new Magnet("correct","correct answers",0));
            list.add(new Magnet("end","end events",0));
            list.add(new Magnet("falling","falling",0));
            list.add(new Magnet("heartbeats","heartbeats",0));
            list.add(new Magnet("siren","alarming",0));
            list.add(new Magnet("snoring","snoring",0));
            list.add(new Magnet("start","start events",0));

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