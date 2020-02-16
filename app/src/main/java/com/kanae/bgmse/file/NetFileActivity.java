package com.kanae.bgmse.file;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.ui.act.ActView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class NetFileActivity extends AppCompatActivity {

    List<TextPair> reslist;

    LinearLayout linearLayout;

    String server_path = "http://101.133.217.228/";
    String list_name = "reslist.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_control_panel);

        reslist = new ArrayList<>();

        linearLayout = (LinearLayout)findViewById(R.id.main_layout);

        new Thread(networkTask).start();
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {

        }
    });


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refView();
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            List<TextPair> netlist;
            try  {
                URL listurl = new URL(server_path + list_name);
                URLConnection conn = listurl.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                Gson gson = new Gson();
                String strJson = "";

                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                strJson = br.readLine();
                netlist = gson.fromJson(strJson, new TypeToken<List<TextPair>>(){
                }.getType());

                Message msg = new Message();
                reslist = netlist;
                handler.sendMessage(msg);
            } catch(java.net.MalformedURLException e){
                e.printStackTrace();
            } catch(java.io.IOException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };


    private void addView(TextPair textPair){
        ActView child = new ActView(this, textPair.label,textPair.content);
        linearLayout.addView(child);
    }

    private void initView(){
        for(TextPair tp:reslist){
            addView(tp);
        }
    }

    private void refView(){
        linearLayout.removeAllViews();
        initView();
    }

    class TextPair{
        String label;
        String content;
    }


}


