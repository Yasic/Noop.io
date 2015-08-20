package com.example.esir.enotepad;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ESIR on 2015/5/29.
 */
public class Myadapterfornote extends BaseAdapter {
    private Context context;
    private List<Note> Note;

    public  Myadapterfornote(Context context,List<Note> Note){
        this.context = context;
        this.Note = Note;
    }

    @Override
    public int getCount(){
        return (Note==null)?0:Note.size();
    }

    @Override
    public Object getItem(int position) {
        return Note.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView notetitle;
        TextView notebody;
        TextView notetime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Note Note = (Note)getItem(position);
        ViewHolder viewholder = null;
        if(convertView == null){//自定义listview存在数据乱序问题
            convertView = LayoutInflater.from(context).inflate(R.layout.noteitem_cardview,null);
        }
        viewholder = new ViewHolder();
        viewholder.notetitle = (TextView)convertView.findViewById(R.id.notetitle_cardview);
        viewholder.notebody = (TextView)convertView.findViewById(R.id.notebody_cardview);
        viewholder.notetime = (TextView)convertView.findViewById(R.id.notetime_cardview);
        viewholder.notetitle.setText(Note.title);
        viewholder.notebody.setText(Note.body);
        viewholder.notetime.setText(Note.time);
        return convertView;
    }
}
