package com.kanae.bgmse.magnet;

public class Magnet {
    public static final int TYPE_BGM = 0;
    public static final int TYPE_SE = 1;
    public static final int TYPE_UNDEFINED = 2;

    private int type;
    private String label;
    private String content;

    public  Magnet(String label, String content, int type){
        this.label = label;
        this.content = content;
        this.type = type;
    }

    public String getLabel(){
        return label;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
