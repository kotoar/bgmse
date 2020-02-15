package com.kanae.bgmse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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


        initViewPager();

        main_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bgmse";
        initFile();

    }




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


    private static final String SEPARATOR = File.separator;//路径分隔符
    public static void copyFilesFromRaw(Context context, int id, String fileName,String storagePath) {
        InputStream inputStream = context.getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
            file.mkdirs();
        }
        readInputStream(storagePath + SEPARATOR + fileName, inputStream);
    }
    /**
     * 读取输入流中的数据写入输出流
     *
     * @param storagePath 目标文件路径
     * @param inputStream 输入流
     */
    public static void readInputStream(String storagePath, InputStream inputStream) {
        File file = new File(storagePath);
        try {
            if (!file.exists()) {
                // 1.建立通道对象
                FileOutputStream fos = new FileOutputStream(file);
                // 2.定义存储空间
                byte[] buffer = new byte[inputStream.available()];
                // 3.开始读文件
                int lenght = 0;
                while ((lenght = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
                    // 将Buffer中的数据写到outputStream对象中
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();// 刷新缓冲区
                // 4.关闭流
                fos.close();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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