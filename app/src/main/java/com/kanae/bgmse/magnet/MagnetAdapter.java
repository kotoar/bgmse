package com.kanae.bgmse.magnet;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kanae.bgmse.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.kanae.bgmse.MainActivity.musicPool;

public class MagnetAdapter extends BaseAdapter {
    private List<Magnet> mData;
    private LayoutInflater mInflater;
    private HashSet<Integer> selectedList = new HashSet<>();
    public boolean selectmode;


    public HashSet<Integer> getSelectedList() {
        return selectedList;
    }

    public MagnetAdapter(Context context, List<Magnet> data) {
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    public void resetSelect() {
        selectedList.clear();

    }

    public void resetData(List<Magnet> data){
        this.mData = data;
    }

    private void reverseSelectMode() {
        selectmode = !selectmode;
    }

    private boolean findSelect(int position){
        return selectedList.contains(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.magnet, null);
        if(convertView !=null){
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.magnet_layout);
            TextView textViewLabel = (TextView) convertView.findViewById(R.id.magnet_label);
            TextView textViewContent = (TextView) convertView.findViewById(R.id.magnet_content);
            final CheckBox selectRadio = (CheckBox) convertView.findViewById(R.id.select_radio);

            selectRadio.setChecked(selectedList.contains(position));

            textViewLabel.setText(mData.get(position).getLabel());
            textViewContent.setText(mData.get(position).getContent());

            if(selectmode){
                selectRadio.setVisibility(View.VISIBLE);
            }
            else{
                selectRadio.setWidth(View.GONE);
            }

            linearLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!selectmode){
                        musicPool.play_se(mData.get(position).getLabel());
                    }
                    else{
                        selectRadio.setChecked(!selectRadio.isChecked());
                        if(!selectedList.contains(position)){
                            selectedList.add(position);
                        }
                        else{
                            selectedList.remove(position);
                        }
                    }
                }
            });

            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent();
                    intent.setAction("action.popupmenu.popup");
                    intent.putExtra("ItemPosition",position);
                    context.sendBroadcast(intent);
                    return true;
                }
            });

        }
        return convertView;
    }



}
