package com.kanae.bgmse.magnet;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class MagnetAdapter extends ArrayAdapter<Magnet> {

    private int resourceId;

    public MagnetAdapter(Context context, int textViewResourceId,
                         List<Magnet> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


}
