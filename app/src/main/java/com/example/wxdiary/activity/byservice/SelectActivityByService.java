package com.example.wxdiary.activity.byservice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.wxdiary.R;
import com.example.wxdiary.adapter.EditAdapter;
import com.example.wxdiary.bean.Diary;
import com.example.wxdiary.helper.Logger;
import com.example.wxdiary.service.MyIntentService;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectActivityByService extends AppCompatActivity{

    private RelativeLayout btn_List;
    private static final int CHECK_ON_BACK = 1;
    private static final int EDIT_ON_BACK = 2;
    private RelativeLayout btn_Edit;
    private List<Diary> diaryList ;
    private AlertDialog checkDialog;
    private AlertDialog editDialog;
    // 注册广播用于对service成功处理请求后的broadcast进行监听
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.D("receive:" + intent.getAction());
            Logger.D(intent.getAction());
            Logger.D(MyIntentService.BROADCAST_READ);
            if(intent.getAction().equals(MyIntentService.BROADCAST_READ)) {
                Logger.D(intent.getSerializableExtra("diaryList").toString());
                // 接受service广播发来的diarylist
                diaryList = (List<Diary>) intent.getSerializableExtra("diaryList");
                sortDiary(diaryList);
                Logger.D("read");
            }
            if(intent.getAction().equals("delete")){
                Logger.D("delete");
            }
            else{
                Logger.D("else");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        setUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.D("onStart");

    }
    // 注册广播
    @Override
    protected void onResume() {
        Logger.D("onResume");
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MyIntentService.BROADCAST_READ));

    }
    // 取消对广播的监听
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void setUI(){
        btn_List = findViewById(R.id.btn_List);
        btn_Edit = findViewById(R.id.btn_Edit);

        btn_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListDialog();
            }
        });
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditDialog();
            }
        });

    }
    private void createListDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ArrayList<String> dates = new ArrayList<>();
        Intent intent = new Intent(SelectActivityByService.this, MyIntentService.class);
        intent.setAction("read");
        startService(intent);
        if(diaryList == null){
            diaryList = new ArrayList<Diary>();
        }
        for (int i = 0; i < diaryList.size(); i++) {
            dates.add(diaryList.get(i).getDates());
        }
        View view = LayoutInflater.from(this).inflate(R.layout.date_select_panel, null);
        ab.setTitle("选择查看的内容");
        ListView listView = view.findViewById(R.id.dates_listview);
        Button btnAdd = view.findViewById(R.id.btn_AddDiary);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SelectActivityByService.this, ReadWriteActivityByService.class), CHECK_ON_BACK);
            }
        });
        /// TODO ： postdelayed用于对提交给service处理后的内容进行延迟显示，因为service处理是需要额外的时间 ，否则会直接报错
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sortDiary(diaryList);
                dates.clear();
                for (int i = 0; i < diaryList.size(); i++) {
                    dates.add(diaryList.get(i).getDates());
                }
                Logger.D(diaryList.get(0).getDates());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SelectActivityByService.this, SingleDiaryShowActivity.class);
                        intent.putExtra("diaryList", (Serializable) diaryList);
                        intent.putExtra("index",position);
                        startActivityForResult(intent, CHECK_ON_BACK);
                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectActivityByService.this, android.R.layout.simple_list_item_1, dates);

                listView.setAdapter(adapter);
            }
        },500);
        ab.setView(view);
        checkDialog = ab.create();
        checkDialog.show();
    }
    /// TODO ：代码复用高，可考虑整合相同的方法
    private void sortDiary(List<Diary> list){
        Collections.sort(list, new Comparator<Diary>() {
            @Override
            public int compare(Diary o1, Diary o2) {
                String[] time1 = o1.getDates().split("-");
                String[] time2 = o2.getDates().split("-");
                Logger.D(Arrays.toString(time1));
                Logger.D(Arrays.toString(time2));
                for (int i = 0; i < time1.length; i++) {
                    if(Integer.parseInt(time1[i]) > Integer.parseInt(time2[i])) {
                        return 1;
                    }
                    if(Integer.parseInt(time1[i]) < Integer.parseInt(time2[i])) {
                        return -1;
                    }
                }
                return 0;
            }
        });
    }
    private void createEditDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("选择要删除的日记");
        View view = LayoutInflater.from(this).inflate(R.layout.diary_edit_panel, null);
        ListView listView = view.findViewById(R.id.edit_listview);
        Intent intent = new Intent(SelectActivityByService.this, MyIntentService.class);
        intent.setAction("read");
        startService(intent);
        if(diaryList == null){
            diaryList = new ArrayList<Diary>();
        }
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sortDiary(diaryList);
                EditAdapter editAdapter = new EditAdapter(SelectActivityByService.this, R.layout.single_item, diaryList);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SelectActivityByService.this, EditActivityByService.class);
                        intent.putExtra("date", diaryList.get(position).getDates());
                        intent.putExtra("content", diaryList.get(position).getContents());
                        startActivityForResult(intent, EDIT_ON_BACK);
                    }
                });
                listView.setAdapter(editAdapter);

            }
        },500);
        ab.setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < diaryList.size(); i++) {
                    View inView = (View) listView.getChildAt(i);
                    CheckBox c = inView.findViewById(R.id.edit_heckbox);
                    if(c.isChecked()){
                        Intent intent = new Intent(SelectActivityByService.this, MyIntentService.class);
                        intent.putExtra("date", diaryList.get(i).getDates());
                        intent.setAction("delete");
                        startService(intent);
                    }

                }
            }
        });
        ab.setView(view);
        editDialog = ab.create();
        editDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHECK_ON_BACK) {
            diaryList = DataSupport.findAll(Diary.class);
            sortDiary(diaryList);
            checkDialog.dismiss();
        }
        if(requestCode == EDIT_ON_BACK) {
            diaryList = DataSupport.findAll(Diary.class);
            sortDiary(diaryList);
        }

    }

}