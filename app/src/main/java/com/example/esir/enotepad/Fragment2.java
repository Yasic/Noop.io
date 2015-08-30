package com.example.esir.enotepad;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePopupWindow;
import com.software.shell.fab.ActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ESIR on 2015/5/29.
 */
public class Fragment2 extends Fragment implements ReminderDialog_setting,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{
    private List<Notebook> Notebook;
    private ActionButton FABbutton;
    private View view;
    private List<Notebook> notebooks = null;
    private Myadapterfornotebook myadapterfornotebook;
    private GridView notebookList_Gridview;
    private TextView tvTime,tvOptions;
    private TimePopupWindow pwTime;
    private String Reminder_Title,Reminder_Description;

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
                ReminderDialog reminderDialog = new ReminderDialog(getActivity(), R.style.notebookDialog);
                reminderDialog.setOnNotebookDialogListener(Fragment2.this);
                reminderDialog.show();
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
    public void onReminderSetting(String Reminder_Title_fromdialog,String Reminder_Description_fromdialog){
        ///todo
        if(Reminder_Title_fromdialog.equals("")&&Reminder_Description_fromdialog.equals("")){
        }
        else{
            Reminder_Title = Reminder_Title_fromdialog;
            Reminder_Description = Reminder_Description_fromdialog;
            tvTime=(TextView)view.findViewById(R.id.tvTime);
            tvTime.setVisibility(View.GONE);
            tvOptions=(TextView)view.findViewById(R.id.tvOptions);
            //时间选择器
            pwTime = new TimePopupWindow(getActivity(), TimePopupWindow.Type.ALL);
            //时间选择后回调
            pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(java.util.Date date) {
                    if(date.before(getSystemTime())){
                        Toast.makeText(getActivity(),"Wrong Time!",Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Toast.makeText(getActivity(),"nothing",Toast.LENGTH_LONG).show();
                        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity(), ReminderAlarm_Service.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Reminder_Title",Reminder_Title);
                        bundle.putString("Reminder_Description",Reminder_Description);
                        intent.putExtras(bundle);
                        //PendingIntent reminderPendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
                        PendingIntent reminderPendingIntent = PendingIntent.getService(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        Calendar setReminderTime = Calendar.getInstance();
                        setReminderTime.setTime(date);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                setReminderTime.getTimeInMillis(), reminderPendingIntent);
                        Toast.makeText(getActivity(),"ok~~",Toast.LENGTH_LONG).show();
                    }
                    //tvTime.setText(getTime(date));
                }
            });
            //弹出时间选择器
            pwTime.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0, new java.util.Date());
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {

    }

    public static String getTime(java.util.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static java.util.Date getSystemTime(){
        SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return curDate;
    }
}
