package com.example.esir.enotepad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by ESIR on 2015/9/29.
 */
public class Testpart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testpart);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);*/
        //StatusBarCompat.compat(this, getResources().getColor(R.color.status_bar_color));
        //StatusBarCompat.compat(this);
    }
}
