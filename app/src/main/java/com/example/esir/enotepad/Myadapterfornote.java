package com.example.esir.enotepad;

import android.content.Context;
import android.graphics.Color;
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
        Getcolors getcolors = new Getcolors();
        //convertView.setBackgroundResource(getcolors.getColor(Integer.valueOf(Note.notecolor)));
        String suducolor = null;
        switch (Note.notecolor){
            case "0":
                suducolor = "#ffffff";
                break;
            case "1":
                suducolor = "#88e51c23";
                break;
            case "2":
                suducolor = "#88e91e63";
                break;
            case "3":
                suducolor = "#88880e4f";
                break;
            case "4":
                suducolor = "#889c27b0";
                break;
            case "5":
                suducolor = "#88673ab7";
                break;
            case "6":
                suducolor = "#883f51b5";
                break;
            case "7":
                suducolor = "#885677fc";
                break;
            case "8":
                suducolor = "#8803a9f4";
                break;
            case "9":
                suducolor = "#8800bcd4";
                break;
            case "10":
                suducolor = "#88009688";
                break;
            case "11":
                suducolor = "#88259b24";
                break;
            case "12":
                suducolor = "#888bc34a";
                break;
            case "13":
                suducolor = "#88cddc39";
                break;
            case "14":
                suducolor = "#88ffeb3b";
                break;
            case "15":
                suducolor = "#88ffc107";
                break;
            case "16":
                suducolor = "#88ff9800";
                break;
            case "17":
                suducolor = "#88ff5722";
                break;
            case "18":
                suducolor = "#88795548";
                break;
        }
        //convertView.setBackgroundColor(Color.parseColor(suducolor));
        convertView.findViewById(R.id.notecolorTextView).setBackgroundColor(Color.parseColor(suducolor));
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
