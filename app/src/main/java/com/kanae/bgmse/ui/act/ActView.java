package com.kanae.bgmse.ui.act;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kanae.bgmse.R;

public class ActView extends LinearLayout {

    LayoutInflater layoutInflater;

    LinearLayout linearLayout;
    TextView textViewLabel;
    TextView textViewContent;

    String label;
    String content;

    public ActView(Context context){
        super(context);
        initView(context);
    }

    public ActView(Context context, String inlabel, String incontent){
        super(context);
        initView(context);
        setText(inlabel,incontent);
    }

    public ActView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initActView(context, attrs);
        initView(context);
    }


    public ActView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initActView(context, attrs);
        initView(context);
    }

    private void initActView(Context context, AttributeSet attrs){
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ActView);
        label = mTypedArray.getString(R.styleable.ActView_Label);
        content = mTypedArray.getString(R.styleable.ActView_Content);
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet, this, true);
        linearLayout = (LinearLayout) findViewById(R.id.magnet_layout);
        textViewLabel = (TextView) findViewById(R.id.magnet_label);
        textViewContent = (TextView) findViewById(R.id.magnet_content);
        if(label != null && content != null){
            setText(label,content);
        }
    }

    public void setText(String inLabel, String inContent){
        textViewLabel.setText(inLabel);
        textViewContent.setText(inContent);
    }

    public void setLabel(String inLabel){
        textViewLabel.setText(inLabel);
    }

    public void setContent(String inContent){
        textViewContent.setText(inContent);
    }

}
