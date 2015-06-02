package com.example.esir.enotepad;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    private String[] data = {"Name","Note","Notebooks","Trash","Settings","About"};
    private ListView listview;
    private List<Drawmenu> drawmenu;
    private List<Note> Note;
    private Button menubutton , plusbutton;
    private GridView notegridview;
    private SoundPool sp;
    public Fragment1 fragment1back,fragment1;
    public Fragment2 fragment2;
    public String title,note,time,flag;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setdefaultfragment();
        title = null;
        note = null;
        time = null;
        init();
        menubutton = (Button)findViewById(R.id.menubutton);
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerlayout;
                drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
                //***********************************************************//
                if (drawerlayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerlayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerlayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        plusbutton = (Button)findViewById(R.id.plusbutton);
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sp = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
                //int music = sp.load(getApplicationContext(),R.raw.clickbutton,1);
                //sp.play(music,1,1,0,0,1);
                Intent intent = new Intent(MainActivity.this, Noteedit.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",title);
                bundle.putString("note",note);
                bundle.putString("time",time);
                intent.putExtras(bundle);
                startActivityForResult(intent, 8080);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8080 && resultCode == 80801)
        {
            Bundle bundle = data.getExtras();
            title = bundle.getString("title");
            note = bundle.getString("note");
            flag = bundle.getString("flag");
            String temptime = bundle.getString("time");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            time = simpleDateFormat.format(new java.util.Date());//get system time
            fragment1 = new  Fragment1();
            Bundle bundles = new Bundle();
            bundles.putString("title",title);
            bundles.putString("note",note);
            bundles.putString("time", time);
            bundle.putString("flag",flag);
            fragment1.setArguments(bundle);//可以发送请求
            getFragmentManager().beginTransaction().replace(R.id.mainlayout, fragment1).commit();
            title = null;
            note = null;
            time = null;
        }
    }

    public void setdefaultfragment(){
        fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("note",note);
        bundle.putString("time", time);
        bundle.putString("flag",flag);
        fragment1.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.mainlayout, fragment1).commit();
        TextView textview = (TextView)findViewById(R.id.actionbar_title);
        textview.setText("Note");
    }

    public void init(){
        drawmenu = new ArrayList<Drawmenu>();
        drawmenu.add(new Drawmenu(R.drawable.ic_file_outline_black_48dp,"Note"));
        drawmenu.add(new Drawmenu(R.drawable.ic_server_black_48dp,"Notebooks"));
        drawmenu.add(new Drawmenu(R.drawable.ic_settings_black_48dp,"Settings"));
        drawmenu.add(new Drawmenu(R.drawable.ic_delete_black_48dp,"Trash"));
        drawmenu.add(new Drawmenu(R.drawable.ic_emoticon_devil_black_48dp, "About"));
        ListAdapter adapter = new Myadapter(this,drawmenu);
        listview = (ListView)findViewById(R.id.menulist);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectitem(position);
            }
        });
    }

    public void selectitem(int position){
            switch (position){
                case 0:
                {
                    fragment1 = new  Fragment1();
                    Bundle bundle = new Bundle();
                    bundle.putString("title",title);
                    bundle.putString("note",note);
                    bundle.putString("time", time);
                    bundle.putString("flag",flag);
                    fragment1.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.mainlayout, fragment1).commit();
                    TextView textview = (TextView)findViewById(R.id.actionbar_title);
                    textview.setText("Note");
                    break;
                }
                case 1:
                {
                    fragment2 = new Fragment2();
                    getFragmentManager().beginTransaction().replace(R.id.mainlayout, fragment2).commit();
                    TextView textview = (TextView)findViewById(R.id.actionbar_title);
                    textview.setText("Notebooks");
                    break;
                }
                case 2:
                {
                    TextView textview = (TextView)findViewById(R.id.actionbar_title);
                    textview.setText("Settings");
                    break;
                }
                case 3:
                {
                    TextView textview = (TextView)findViewById(R.id.actionbar_title);
                    textview.setText("Trash");
                    break;
                }
                case 4:
                {
                    TextView textview = (TextView)findViewById(R.id.actionbar_title);
                    textview.setText("About");
                    break;
                }
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}
