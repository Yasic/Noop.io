package com.example.esir.enotepad;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/5/29.
 */
public class Fragment2 extends Fragment implements NotebookDialog_setting{
    private List<Notebook> Notebook;
    private ActionButton FABbutton;
    private View view;
    private List<Notebook> notebooks = null;
    private Myadapterfornotebook myadapterfornotebook;
    private GridView notebookList_Gridview;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savadInstanceState){
        view = inflater.inflate(R.layout.fragment2,container,false);
        init_FABbutton();
        init_NotebookList();
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
        FABbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotebookDialog notebookDialog = new NotebookDialog(getActivity(), R.style.notebookDialog);
                notebookDialog.setOnNotebookDialogListener(Fragment2.this);
                notebookDialog.show();
            }
        });
    }

    public void init_NotebookList(){
        notebooks = new ArrayList<Notebook>();
        myadapterfornotebook = new Myadapterfornotebook(getActivity(),notebooks);
        notebookList_Gridview = (GridView)view.findViewById(R.id.notebookList);
        notebookList_Gridview.setAdapter(myadapterfornotebook);
    }

    @Override
    public void onSetting(String notebook_title, String notebook_description) {
        //Toast.makeText(getActivity(),"title:"+notebook_title+"\n"+"description:"+notebook_description,Toast.LENGTH_LONG).show();
        notebooks.add(new Notebook(notebook_title,"3",notebook_description,"2015-8-29-18:39","red"));
        myadapterfornotebook.notifyDataSetChanged();
        notebookList_Gridview.invalidateViews();
    }
}
