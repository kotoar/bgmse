package com.kanae.bgmse.file;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kanae.bgmse.magnet.Magnet;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;



public class MagnetSaver {
    String magnet_path;

    public MagnetSaver(){
        magnet_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bgmse/mainlist.txt";
    }


    public void StackSave(List<Magnet> list){
        Gson gson = new Gson();
        String strJson = gson.toJson(list);
        File file = new File(magnet_path);
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file,false);
            fos.write(strJson.getBytes());
            fos.close();
        } catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    public List<Magnet> StackLoad(){
        List<Magnet> loaded;
        Gson gson = new Gson();
        String strJson = "";
        File file = new File(magnet_path);
        try{
            if(!file.exists()){
                return null;
            }

            InputStream inputStream = new FileInputStream(magnet_path);
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            strJson = br.readLine();


        } catch (java.io.IOException e){
            e.printStackTrace();
        }
        loaded = gson.fromJson(strJson, new TypeToken<List<Magnet>>(){
        }.getType());
        return loaded;

    }

    public void AddMagnet(Magnet magnet){
        List<Magnet> stacklist = StackLoad();
        stacklist.add(magnet);
        StackSave(stacklist);
    }

}
