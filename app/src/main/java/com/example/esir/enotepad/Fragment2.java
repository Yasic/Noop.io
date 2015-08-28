package com.example.esir.enotepad;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.software.shell.fab.ActionButton;

import java.util.List;

/**
 * Created by ESIR on 2015/5/29.
 */
public class Fragment2 extends Fragment {
    private List<Notebook> Notebook;
    private ActionButton FABbutton;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savadInstanceState){
        view = inflater.inflate(R.layout.fragment2,container,false);
        init_FABbutton();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8080 && resultCode == 80802) {

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }

    public void init_FABbutton(){
        FABbutton = (ActionButton)view.findViewById(R.id.plusbuttonofnotebook);
        FABbutton.setShowAnimation(ActionButton.Animations.JUMP_FROM_DOWN);//设置动画set
        FABbutton.setHideAnimation(ActionButton.Animations.JUMP_TO_DOWN);//设置动画set
        FABbutton.setImageDrawable(getResources().getDrawable(R.drawable.fab_plus_icon));//设置background
        FABbutton.setButtonColor(getResources().getColor(R.color.fab_mdcolor));
    }
}
