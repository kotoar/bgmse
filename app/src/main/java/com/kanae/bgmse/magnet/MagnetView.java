package com.kanae.bgmse.magnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kanae.bgmse.R;

import org.w3c.dom.Text;

public class MagnetView extends LinearLayout {

    Magnet data;
    LayoutInflater layoutInflater;

    LinearLayout linearLayout;
    TextView textViewLabel;
    TextView textViewContent;
    int seid;

    public MagnetView(Context context, String label, String content, int type,int inseid){
        super(context);
        data = new Magnet(label,content,type);
        initView(context);
        seid = inseid;
        setData();
    }

    public MagnetView(Context context, Magnet magnet,int inseid){
        super(context);
        data = new Magnet(magnet.getLabel(),magnet.getContent(),magnet.getType());
        initView(context);
        seid = inseid;
        setData();
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet, this, true);
        linearLayout = (LinearLayout) findViewById(R.id.magnet_layout);
        textViewLabel = (TextView) findViewById(R.id.magnet_label);
        textViewContent = (TextView) findViewById(R.id.magnet_content);
    }

    public void setData(){
        textViewLabel.setText(data.getLabel());
        textViewContent.setText(data.getContent());

    }

    public final int getSeid() {
        return seid;
    }
}
