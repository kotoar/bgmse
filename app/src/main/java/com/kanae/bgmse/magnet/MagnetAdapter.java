package com.kanae.bgmse.magnet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kanae.bgmse.R;

import java.util.List;

public class MagnetAdapter extends ArrayAdapter<Magnet> {

    private int resourceId;
    private int magnetCrt;

    public MagnetAdapter(Context context, int textViewResourceId,
                         List<Magnet> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        magnetCrt = objects.size();
    }

    public View getView(int position, View convertView, ViewGroup parent){

        Magnet magnet = getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout) view.findViewById(R.id.magnet_layout);
            viewHolder.maglabel = (TextView) view.findViewById(R.id.magnet_label);
            viewHolder.magcontent = (TextView) view.findViewById(R.id.magnet_content);
            view.setTag(viewHolder);

        } else {
            //convertView参数用于将之前加载好的布局进行缓存，以便之后可以进行复用（提高效率）
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }



        return view;
    }

    @Override
    public int getCount(){
        return magnetCrt;
    }

    /*private void sendLocalBroadCastToUpdate(int position) {
        Intent intent = new Intent(Fragment_VpItem.);
        intent.putExtra(Fragment_VpItem.KEY_POSITION, position);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }*/

    class ViewHolder{
        LinearLayout layout;
        TextView maglabel;
        TextView magcontent;
    }
}
