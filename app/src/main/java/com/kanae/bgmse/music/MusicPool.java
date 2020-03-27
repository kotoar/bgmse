package com.kanae.bgmse.music;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;

import com.kanae.bgmse.R;
import com.kanae.bgmse.file.MagnetSaver;
import com.kanae.bgmse.magnet.Magnet;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kanae.bgmse.MainActivity.musicPool;

public class MusicPool {

    private List<Magnet> resList = new ArrayList<>();
    private List<Magnet> favList = new ArrayList<>();
    private SoundPool mSoundPool = null;
    private HashMap<String, Integer> soundID = new HashMap<String, Integer>();
    private String main_path = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/bgmse/se/";

    private MagnetSaver magnetSaver = new MagnetSaver();

    public MusicPool(){
        initMusic();
        loadmusic();
        initFavList();
    }

    public List<Magnet> getMagnetList(){
        return resList;
    }

    public void setFav(String indexLabel){
        for(Magnet m:resList){
            if(m.getLabel().equals(indexLabel)){
                m.reverseIsfav();
                break;
            }
        }
    }

    public void resFavList(){
        favList.clear();
        for(Magnet m:resList){
            if(m.getIsfav() == 1){
                favList.add(m);
            }
        }
    }

    private void initFavList(){
        for(Magnet m:resList){
            if(m.getIsfav() == 1){
                favList.add(m);
            }
        }
    }

    public List<Magnet> getFavList(){
        return favList;
    }

    public HashMap<String,Integer> getSoundID(){
        return soundID;
    }

    private void initMusic() {
        resList = magnetSaver.StackLoad();
    }

    private void loadmusic(){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        for(int i=0;i<resList.size();i++){
            soundID.put(resList.get(i).getLabel(), mSoundPool.load( main_path+resList.get(i).getLabel()+".mp3", 1));
        }
    }

    public void play_se(String indexLabel){
        mSoundPool.play(soundID.get(indexLabel),1,1,0,0,1);
    }

    public void deleteRes(String indexLabel){
        for(Magnet m:resList){
            if(m.getLabel().equals(indexLabel)){
                resList.remove(m);
                break;
            }
        }
    }


}
