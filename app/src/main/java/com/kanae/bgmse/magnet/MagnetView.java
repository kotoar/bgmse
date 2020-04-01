package com.kanae.bgmse.magnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kanae.bgmse.R;

import java.util.List;

public class MagnetView extends LinearLayout {



    Magnet data;
    LayoutInflater layoutInflater;

    LinearLayout linearLayout;
    TextView textViewLabel;
    TextView textViewContent;

    CheckBox selectRadio;

    private boolean isselected;
    private int select_mode;

    public MagnetView(Context context, Magnet magnet){
        super(context);
        data = magnet;
        initView(context);
        setData();
        isselected = false;
        select_mode = 0;
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet, this, true);
        linearLayout = (LinearLayout) findViewById(R.id.magnet_layout);
        textViewLabel = (TextView) findViewById(R.id.magnet_label);
        textViewContent = (TextView) findViewById(R.id.magnet_content);
        selectRadio = (CheckBox) findViewById(R.id.select_radio);
    }

    public void setData(){
        textViewLabel.setText(data.getLabel());
        textViewContent.setText(data.getContent());
    }

    public int getMusicFun(){
        return data.getMusicFun();
    }

    public String getLabel(){
        return data.getLabel();
    }

    public void setFavColor(){
        textViewLabel.setTextColor(this.getResources().getColor(R.color.colorFavHighlight));
    }

    public void startSelect(){
        select_mode = 1;
        selectRadio.setWidth(35);

    }

    public void endSelect(){
        select_mode = 0;
        selectRadio.setWidth(0);
    }

    public void setIsselected(boolean set){
        isselected = set;
    }

    public boolean getIsselect(){
        return isselected;
    }


}
