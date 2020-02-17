package com.kanae.bgmse.file;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kanae.bgmse.R;
import com.kanae.bgmse.magnet.Magnet;
import com.kanae.bgmse.music.MusicPool;
import com.kanae.bgmse.ui.act.ActView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.kanae.bgmse.MainActivity.musicPool;

public class NetFileActivity extends AppCompatActivity {

    List<TextPair> reslist;
    List<Magnet> novlist;
    MagnetSaver magnetSaver;

    LinearLayout linearLayout;

    final String server_path = "http://101.133.217.228/";
    final String list_name = "reslist.txt";
    String storage_path;
    TextPair chosentp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_control_panel);

        reslist = new ArrayList<>();
        storage_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bgmse/se/";

        linearLayout = (LinearLayout)findViewById(R.id.main_layout);

        magnetSaver = new MagnetSaver();
        novlist = magnetSaver.StackLoad();


        new Thread(netListUpdate).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                refView();
            }
            if(msg.what ==2){
                musicPool = new MusicPool();
                refreshFrag1();
                AlertDialog diag = new AlertDialog.Builder(NetFileActivity.this)
                        .setTitle("bgmse")
                        .setMessage("Downloaded")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                diag.show();
            }
        }
    };

    Runnable netListUpdate = new Runnable() {

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
                msg.what = 1;
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

    Runnable dlthread = new Runnable() {

        @Override
        public void run() {
            try{
                URL dlurl = new URL(server_path + chosentp.label + ".mp3");
                URLConnection dlconn = dlurl.openConnection();
                dlconn.connect();
                InputStream dlis = dlconn.getInputStream();

                File file = new File(storage_path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outfile = new File(storage_path + chosentp.label + ".mp3");

                if (!outfile.exists()) {
                    OutputStream fos = new FileOutputStream(outfile);
                    byte[] buffer = new byte[1024];
                    int lenght = 0;
                    while ((lenght = dlis.read(buffer)) != -1) {
                        fos.write(buffer, 0, lenght);
                    }
                    fos.flush();
                    fos.close();
                    dlis.close();
                }


                int updnum = novlist.get(novlist.size()-1).getMusicFun() + 1;
                novlist.add(new Magnet(chosentp.label,chosentp.content, 0, updnum));
                magnetSaver.StackSave(novlist);

                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);


            } catch (java.io.FileNotFoundException e){
                e.printStackTrace();
            } catch (java.net.MalformedURLException e){
                e.printStackTrace();
            } catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
    };

    private void download(TextPair textPair){
        chosentp = textPair;
        new Thread(dlthread).start();
    }

    private void addView(final TextPair textPair){
        ActView child = new ActView(this, textPair.label,textPair.content);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(textPair);
            }
        });
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

    private void refreshFrag1(){
        Intent intent = new Intent();
        intent.setAction("action.refreshSEView");
        sendBroadcast(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class TextPair{
        String label;
        String content;
    }

}


