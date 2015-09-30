package com.example.esir.enotepad

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

/**
 * Created by ESIR on 2015/9/30.
 */
public class Kotlintest: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myMessage = TextView(this)
        myMessage.setText("Hello")
        setContentView(myMessage)
    }
}