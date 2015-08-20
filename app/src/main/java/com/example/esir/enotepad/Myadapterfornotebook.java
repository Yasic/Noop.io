package com.example.esir.enotepad;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.Base;

import java.util.List;

/**
 * Created by ESIR on 2015/6/6.
 */
public class Myadapterfornotebook extends BaseAdapter {
    private Context context;
    private List<Notebook> Notebook;

    public  Myadapterfornotebook(Context context,List<Notebook> Notebook){
        this.context = context;
        this.Notebook = Notebook;
    }

    @Override
    public int getCount(){
        return (Notebook == null)?0:Notebook.size();
    }

    @Override
    public Object getItem(int position) {
        return Notebook.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView notebookname;
        TextView notebooktime;
        TextView notebookdescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }

}
