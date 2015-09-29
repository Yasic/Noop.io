package com.example.esir.enotepad;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.software.shell.fab.ActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{
    //private String[] data = {"Name","Note","Notebooks","Trash","Settings","About"};
    private ListView listview;
    private List<Drawmenu> drawmenu;//侧边栏菜单
    //private List<Note> Note;//笔记列表
    private Button menubutton , plusbutton , sortbutton;//actionbar上的两个按钮
    private String username,email,password;//昵称、邮箱、密码
    private TextView usernametext;
    public Fragment1 fragment1;//碎片
    public Fragment2 fragment2;
    public String title,note,time,flag,edittime;//分别为笔记标题、笔记内容、笔记时间、笔记状态标记
    //private FragmentManager fragmentManager;
    private AVObject testclass;
    private ActionButton fabbutton;
    private Integer sortFlag = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_right_in_anim, R.anim.still_nothing_anim);//activity切换动画
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(getApplicationContext(), "16se9yws3bbmlnqluefpoone4pyllqsojvu7aayzfowi44su", "nz8gbcvz7zfy1zwqy9zpad2hg2kfoybpi1oeobji48dmzvvi");
        setdefaultfragment();//默认碎片设置
        title = null;
        note = null;
        time = null;
        init();//初始化，实现侧边栏菜单监听
        addbuttonlistener();//添加按钮实例和按钮监听
        //addfabbutton();

        TextView actionbar_title = (TextView)findViewById(R.id.actionbar_title);
        Typeface typeface = Typeface.createFromAsset(getBaseContext().getAssets(), "Fonts/Bookman_Old_Style.TTF");
        actionbar_title.setTypeface(typeface);
        Getfonts getfonts = new Getfonts(getApplication());
        actionbar_title.setTypeface(getfonts.get_Futura_Book());
    }

    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void init(){

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);*/

        ViewGroup contentView = (ViewGroup) MainActivity.this.findViewById(android.R.id.content);
        TextView contentview = (TextView)findViewById(R.id.contentView);
        contentview.setHeight(contentView.getHeight()+dp2pix(16));
        /*View statusBarView = new View(MainActivity.this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(MainActivity.this));
        statusBarView.setBackgroundColor( Color.parseColor("#3399ff"));
        contentView.addView(statusBarView, lp);*/

        setdrawmenu();//添加侧边栏按钮
        ListAdapter adapter = new Myadapter(this,drawmenu);
        listview = (ListView)findViewById(R.id.menulist);//获取menulist实例
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectitem(position);//侧边栏选项监听动作
            }
        });

        Button menuButton = (Button)findViewById(R.id.menubutton);
        TextView actionbar_title = (TextView)findViewById(R.id.actionbar_title);
        Button syncbutton = (Button)findViewById(R.id.syncbutton);
        syncbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Testpart.class);
                startActivity(intent);
            }
        });
        Button sortbutton = (Button)findViewById(R.id.sortbutton);
        DisplayMetrics metrics = new DisplayMetrics();
        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(menuButton,"translationX",-screenWidth,0);
        objectAnimator.setDuration(2000);
        objectAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator.start();
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(sortbutton,"translationX",-screenWidth,0);
        objectAnimator1.setDuration(2000);
        objectAnimator1.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator1.start();
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(syncbutton,"translationX",-screenWidth,0);
        objectAnimator2.setDuration(2000);
        objectAnimator2.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator2.start();
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(actionbar_title,"translationX",-screenWidth,0);
        objectAnimator3.setDuration(2000);
        objectAnimator3.setInterpolator(new AnticipateOvershootInterpolator());
        objectAnimator3.start();
    }

    public void inituserinfo(){
        inituserinfo();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        email = bundle.getString("email");
        password = bundle.getString("password");
        usernametext = (TextView)findViewById(R.id.usernametext);//获取到姓名显示栏
        usernametext.setText(username);
    }

    public void addbuttonlistener(){//添加按钮实例和按钮监听
        menubutton = (Button)findViewById(R.id.menubutton);
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerlayout;
                drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);//获取侧边栏实例
                if (drawerlayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerlayout.closeDrawer(Gravity.LEFT);//关闭
                } else {
                    drawerlayout.openDrawer(Gravity.LEFT);//开启
                }
            }
        });
        sortbutton = (Button)findViewById(R.id.sortbutton);
        sortbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortFlag == 1) {
                    starfragment1("notecolor");
                    sortFlag = -1;
                } else if (sortFlag == -1) {
                    starfragment1("time");
                    sortFlag = 1;
                }
            }
        });
        //plusbutton = (ImageButton)findViewById(R.id.plusbutton);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)//activity回调
    {
        fabbutton.setButtonColor(getResources().getColor(R.color.fab_mdcolor));//颜色变回来
        fabbutton.playShowAnimation();//fab按钮出现动画
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8080 && resultCode == 80801)
        {
            Bundle bundle = data.getExtras();
            title = bundle.getString("title");
            note = bundle.getString("note");
            flag = bundle.getString("flag");
            time = bundle.getString("time");
            edittime = bundle.getString("edittime");
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //time = simpleDateFormat.format(new java.util.Date());//get system time
            starfragment1("time");//启动fragment1
            title = null;
            note = null;
            time = null;
            edittime = null;
        }
    }

    public void setdefaultfragment(){
        title = null;
        note = null;
        time = null;
        flag = "-1";
        starfragment1("time");//启动fragment1
        TextView textview = (TextView)findViewById(R.id.actionbar_title);
        textview.setText("Note");
    }

    public void setdrawmenu(){
        drawmenu = new ArrayList<Drawmenu>();
        drawmenu.add(new Drawmenu(R.drawable.ic_file_outline_black_48dp,"Note"));
        drawmenu.add(new Drawmenu(R.drawable.ic_server_black_48dp, "Reminders"));
        drawmenu.add(new Drawmenu(R.drawable.ic_settings_black_48dp,"Settings"));
        drawmenu.add(new Drawmenu(R.drawable.ic_delete_black_48dp, "Trash"));
        drawmenu.add(new Drawmenu(R.drawable.ic_emoticon_devil_black_48dp, "About"));
    }

    public void selectitem(int position){
        DrawerLayout drawerlayout;
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);//获取侧边栏实例
        if (drawerlayout.isDrawerOpen(Gravity.LEFT)) {
            drawerlayout.closeDrawer(Gravity.LEFT);//关闭
        }
        switch (position){
            case 0:
            {
                starfragment1("time");//启动fragment1
                TextView textview = (TextView)findViewById(R.id.actionbar_title);
                textview.setText("Note");
                break;
            }
            case 1:
            {
                fragment2 = new Fragment2();
                getFragmentManager().beginTransaction().replace(R.id.mainlayout, fragment2).commit();
                TextView textview = (TextView)findViewById(R.id.actionbar_title);
                textview.setText("Reminders");
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

    public void starfragment1(String sort_Flag){
        fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("note",note);
        bundle.putString("time", time);
        bundle.putString("edittime", edittime);
        bundle.putString("sortflag", sort_Flag);
        //Log.i("time4",time);
        bundle.putString("flag", flag);
        fragment1.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.mainlayout, fragment1).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){//返回键返回上一个activity
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Sure to leave?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {

    }

    private int dp2pix(int dp){
        final float scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp * scale + 0.5f);
        return pix;
    }
}
