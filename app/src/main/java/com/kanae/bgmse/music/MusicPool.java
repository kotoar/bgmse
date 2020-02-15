package com.kanae.bgmse.music;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Environment;

import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicPool {

    private List<Magnet> resList = new ArrayList<>();
    private SoundPool mSoundPool = null;
    private AssetManager aManager;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private String main_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/bgmse/se/";


    public MusicPool(){
        initMusic();
        loadmusic();
    }

    public List<Magnet> getMagnetList(){
        return resList;
    }

    public HashMap<Integer,Integer> getSoundID(){
        return soundID;
    }

    private void initMusic() {
        try{
            InputStream inputStream = new FileInputStream(
                    Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/bgmse" + File.separator+"/datactrl/main_list.txt");
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String string = "";

            while ((string = br.readLine()) != null) {
                InputStream mis = new FileInputStream(
                        Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/bgmse" + File.separator+"/datactrl/" + string + ".txt");
                InputStreamReader misr = new InputStreamReader(mis, "UTF-8");
                BufferedReader mbr = new BufferedReader(misr);
                String mcontent = mbr.readLine();
                int mtype = Integer.parseInt(mbr.readLine());
                Magnet mmag = new Magnet(string,mcontent,mtype);
                resList.add(mmag);
            }
        } catch(java.io.IOException e){
            e.printStackTrace();
        }
    }

    private void loadmusic(){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        for(int i=0;i<resList.size();i++){
            soundID.put(i, mSoundPool.load( main_path+resList.get(i).getLabel()+".mp3", 1));
        }
    }

    public void play_se(int index){
        mSoundPool.play(soundID.get(index),1,1,0,0,1);
    }

}
