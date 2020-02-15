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
        File file = new File(main_path + File.separator+"/datactrl");
        if(!file.exists()){
            InputStream inputStream = getResources().openRawResource(R.raw.init_music_list);
            List<Magnet> init_pool = new ArrayList<>();
            try {
                InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String string = "";
                String label="", content="";
                int type;

                int i=0;
                while ((string = br.readLine()) != null) {
                    i++;
                    if(i%3==1){
                        label = string;
                    }
                    if(i%3==2){
                        content = string;
                    }
                    if(i%3==0){
                        type = Integer.valueOf(string).intValue();
                        Magnet magnet = new Magnet(label,content,type);
                        init_pool.add(magnet);
                    }
                }


                File osfile = new File(main_path + File.separator +"/datactrl/main_list.txt");
                File osParent = osfile.getParentFile();
                osParent.mkdirs();
                osfile.createNewFile();
                FileOutputStream fos = new FileOutputStream(osfile);
                for(Magnet m:init_pool){
                    String inlabel = m.getLabel() + '\n';
                    fos.write(inlabel.getBytes());
                    File mfile = new File(main_path + "/datactrl/" + m.getLabel() + ".txt");
                    mfile.createNewFile();
                    FileOutputStream mfos = new FileOutputStream(mfile);
                    String incontent = m.getContent() + '\n';
                    mfos.write(incontent.getBytes());
                    String intype = String.valueOf(m.getType()) + '\n';
                    mfos.write(intype.getBytes());
                    mfos.close();
                }
                fos.close();

                copyFilesFromRaw(this,R.raw.sta,"sta.mp3",
                        main_path+File.separator+"/se");
                copyFilesFromRaw(this,R.raw.stb,"stb.mp3",
                        main_path+File.separator+"/se");


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }


}