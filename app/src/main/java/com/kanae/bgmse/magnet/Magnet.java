package com.kanae.bgmse.magnet;

public class Magnet {
    public static final int TYPE_BGM = 0;
    public static final int TYPE_SE = 1;
    public static final int TYPE_UNDEFINED = 2;

    private int type;
    private int isfav;
    private String label;
    private String content;
    private int musicFun;
    private boolean isselected;

    public  Magnet(String label, String content, int type, int musicFun){
        this.label = label;
        this.content = content;
        this.type = type;
        this.isfav = 0;
        this.musicFun = musicFun;
        isselected = false;
    }

    public String getLabel(){
        return label;
    }

    public void setLabel(String labelin){
        label = labelin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String contentin) {
        content = contentin;
    }

    public int getType() {
        return type;
    }

    public void setIsfav(int inisfav){
        isfav = inisfav;
    }

    public int getIsfav(){
        return isfav;
    }

    public boolean getIsselected(){
        return isselected;
    }

    public void setIsselected(boolean inisselected){
        isselected = inisselected;
    }

    public void reverseIsfav(){
        if(isfav == 0){
            isfav = 1;
        }else{
            isfav = 0;
        }
    }

    public int getMusicFun(){
        return musicFun;
    }
}
