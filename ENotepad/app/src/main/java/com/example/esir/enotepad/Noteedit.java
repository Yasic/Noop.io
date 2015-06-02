package com.example.esir.enotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.KeyException;

/**
 * Created by ESIR on 2015/5/30.
 */
public class Noteedit extends Activity{
    private String title;
    private String note;
    private String time;
    private EditText titleedittext,noteedittext;
    private Button notesavebutton,notebackbutton,notedeletebutton;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noteedit);
        init();
        savefunction();
        backandpost();
        deletefunction();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            returnactivity();
        }
        return false;
    }

    public void deletefunction(){
        notedeletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Noteedit.this)
                        .setMessage("DELETE?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                title = "";
                                note = "";
                                returnactivity();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
    }

    public void init(){
        notedeletebutton = (Button)findViewById(R.id.notedeletebutton);
        notedeletebutton.setVisibility(View.GONE);
        flag = "0";//默认新建
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        note = bundle.getString("note");
        time = bundle.getString("time");//获取时间
        titleedittext = (EditText)findViewById(R.id.edittitle);
        noteedittext = (EditText)findViewById(R.id.editbody);
        if(title != null | note != null){
            titleedittext.setText(title);
            noteedittext.setText(note);
            notedeletebutton.setVisibility(View.VISIBLE);
            flag = "1";//1则修改
        }
    }

    public void savefunction(){
        notesavebutton = (Button)findViewById(R.id.notesavebutton);
        notesavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Noteedit.this);
                builder.setMessage("Are you sure to save?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title = titleedittext.getText().toString();
                        note = noteedittext.getText().toString();
                        if(title.equals("")&note.equals("")) {
                            Toast.makeText(getApplicationContext(),"Did you forget write note?",Toast.LENGTH_LONG).show();
                        }
                        else{
                            returnactivity();
                        }
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNeutralButton("reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        titleedittext.setText("");
                        noteedittext.setText("");
                    }
                });
                builder.create().show();
            }
        });
    }

    public void backandpost(){
        notebackbutton = (Button)findViewById(R.id.notebackbutton);
        notebackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnactivity();
            }
        });
    }

    public void returnactivity(){
        Intent intent = new Intent(Noteedit.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("note",note);
        bundle.putString("time",time);
        bundle.putString("flag",flag);//1则修改，0则新建
        intent.putExtras(bundle);
        setResult(80801, intent);
        finish();
    }
}
