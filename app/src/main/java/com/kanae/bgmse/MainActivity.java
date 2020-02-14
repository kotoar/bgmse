package com.kanae.bgmse;

import android.app.Activity;
import android.content.Context;
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

import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.ui.main.SectionsPagerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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


        initViewPager();

        main_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bgmse";
        initFile();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (java.io.IOException e){
                e.printStackTrace();
            }


        }



    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}