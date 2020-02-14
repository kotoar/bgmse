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


    public MagnetView(Context context, String label, String content, int type){
        super(context);
        data = new Magnet(label,content,type);
        initView(context);
        setData();
    }

    public MagnetView(Context context, Magnet magnet){
        super(context);
        data = new Magnet(magnet.getLabel(),magnet.getContent(),magnet.getType());
        initView(context);
        setData();
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet, this, true);
        linearLayout = (LinearLayout) findViewById(R.id.magnet_layout);
        textViewLabel = (TextView) findViewById(R.id.magnet_label);
        textViewContent = (TextView) findViewById(R.id.magnet_content);
    }

    private void setData(){
        textViewLabel.setText(data.getLabel());
        textViewContent.setText(data.getContent());

    }
}
