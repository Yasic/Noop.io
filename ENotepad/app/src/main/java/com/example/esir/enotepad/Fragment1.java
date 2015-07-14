package com.example.esir.enotepad;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by ESIR on 2015/5/29.
 */
public class Fragment1 extends Fragment {
    private GridView notegridview;
    private View view;
    private  ListAdapter adapter;
    private TextView testtext;
    private SQLiteDatabase db;
    private ENoteSQLitedbhelper helper,helperinside;
    public List<Note> Note;
    public String title,note,time,flag;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savadInstanceState){
        view = inflater.inflate(R.layout.fragment1,container,false);
        additemlistener();//添加长按短按监听
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)//fragment开启的activity要由fragment来接收！此处接收noteedit返回数据
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8080 && resultCode == 80801) {
            Bundle bundle = data.getExtras();
            getintent(bundle);//获取intent值

            if(flag.equals("1")){//为1修改，为0新建
                if(!title.equals("") | !note.equals("")) {//这里判定条件为空而不是null
                    updatadatabase(bundle);//更新数据库
                }
                else{
                    deletedatabase(bundle);//删除数据库
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Bundle bundle = getArguments();
        getintent(bundle);//获取intent的data值
        Note = new ArrayList<Note>();
        helper = new ENoteSQLitedbhelper(getActivity(),"ENote",1);//开启数据库
        dboutput(helper);
        if(title!=null | note!=null) {//这里接收mainactivity传递的东西,如果不为空则需要新建插入到数据库
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            //time = simpleDateFormat.format(new java.util.Date());//get system time
            Note.add(new Note(title, note,bundle.getString("edittime")));
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("note",note);
            values.put("time",bundle.getString("edittime"));
            helper.getWritableDatabase().insert("notetable",null,values);//插入新的数据
        }
        helper.close();//此处关闭数据库
        adapter = new Myadapterfornote(getActivity(),Note);//以Note生成adapter
    }

    public void deletedatabase(Bundle bundle){
        helperinside = new ENoteSQLitedbhelper(getActivity(), "ENote", 1);//打开数据库
        helperinside.getWritableDatabase().delete("notetable", "time = ?", new String[]{bundle.getString("time")});//删除数据库
        renew(helperinside);//更新gridview
        helperinside.close();//关闭数据库
        title = null;
        note = null;
        time = null;
    }

    public void updatadatabase(Bundle bundle){
        helperinside = new ENoteSQLitedbhelper(getActivity(), "ENote", 1);//打开数据库
        ContentValues cv = new ContentValues();
        cv.put("title",title);
        cv.put("note",note);
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //time = simpleDateFormat.format(new java.util.Date());//get system time
        cv.put("time", bundle.getString("edittime"));
        helperinside.getWritableDatabase().update("notetable", cv , "time = ?", new String[]{bundle.getString("time")});//更新数据库
        renew(helperinside);//更新gridview
        helperinside.close();//关闭数据库
        title = null;
        note = null;
        time = null;
    }

    public void dboutput(ENoteSQLitedbhelper h){//数据库数据遍历输出
        Cursor cursor = h.getReadableDatabase().rawQuery("SELECT * FROM notetable", null);//这句报错
        while (cursor.moveToNext()) {
            Note.add(new Note(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
    }

    public void renew(ENoteSQLitedbhelper h){
        List<Note> TempNote = new ArrayList<Note>();//创建临时list
        Cursor cursor = h.getReadableDatabase().rawQuery("SELECT * FROM notetable", null);//开启游标
        while(cursor.moveToNext()){
            TempNote.add(new Note(cursor.getString(1), cursor.getString(2), cursor.getString(3)));//添加到list
        }
        ListAdapter tempadapter = new Myadapterfornote(getActivity(),TempNote);//生成adapter
        notegridview.setAdapter(tempadapter);//绑定adapter
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        notegridview.setAdapter(adapter);
    }

    public void  additemlistener(){
        notegridview = (GridView)view.findViewById(R.id.notegridview);//获取gridview实例
        notegridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("DELETE?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helperinside = new ENoteSQLitedbhelper(getActivity(), "ENote", 1);//开启数据库
                                TextView timetext = (TextView) view.findViewById(R.id.notetime);
                                String s = timetext.getText().toString();
                                helperinside.getWritableDatabase().delete("notetable", "time = ?", new String[]{s});
                                renew(helperinside);
                                helperinside.close();
                            }
                        })
                        .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                return true;//避免同时触发两个监听，长按返回true则短按不会触发
            }
        });
        notegridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo
                //实现开启编辑通信
                Intent intent = new Intent(getActivity(), Noteedit.class);
                Bundle bundle = new Bundle();
                TextView timetext = (TextView) view.findViewById(R.id.notetitle);
                String s = timetext.getText().toString();
                bundle.putString("title", s);
                timetext = (TextView) view.findViewById(R.id.notebody);
                s = timetext.getText().toString();
                bundle.putString("note", s);
                timetext = (TextView) view.findViewById(R.id.notetime);
                s = timetext.getText().toString();
                bundle.putString("time", s);
                timetext = null;
                s = null;
                intent.putExtras(bundle);
                startActivityForResult(intent, 8080);
            }
        });
    }

    public void getintent(Bundle bundle){
        title = bundle.getString("title");
        note = bundle.getString("note");
        time = bundle.getString("time");
        flag = bundle.getString("flag");
    }
}
