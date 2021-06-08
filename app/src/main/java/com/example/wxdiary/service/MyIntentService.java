package com.example.wxdiary.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.wxdiary.bean.Diary;
import com.example.wxdiary.data.DataControl;
import com.example.wxdiary.helper.Logger;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyIntentService extends IntentService {

    public static final String BROADCAST_READ = "com.example.wxdiary.service.read";
    public static final String BROADCAST_DELETE = "com.example.wxdiary.service.delete";
    public static final String BROADCAST_UPDATE = "com.example.wxdiary.service.update";
    private List<Diary> diaryList;
    private Intent intent1;


    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // 根据intent的action类型来区分操作
        checkDataSupport();
        if (intent != null) {
            Logger.D(intent.getAction());
            Logger.D("intent !=  null");
            if(intent.getAction().equals("read")) {
                intent1 = new Intent(BROADCAST_READ);
                diaryList = DataSupport.findAll(Diary.class);
                sortDiary(diaryList);
                intent1.putExtra("diaryList", (Serializable) diaryList);
                sendBroadcast(intent1);
            }
            if(intent.getAction().equals("delete")){
                intent1 = new Intent(BROADCAST_DELETE);
                intent1.setAction("delete");
                String date = intent.getStringExtra("date");
                Diary.deleteAll(Diary.class, "dates = ?", date);
                sendBroadcast(intent1);
            }
            if(intent.getAction().equals("update")){
                Logger.D("Service: update");
                String dates = intent.getStringExtra("dates");
                String contents = intent.getStringExtra("contents");
                Diary diary = Diary.where("dates = ?",dates).find(Diary.class).get(0);
                Logger.D(diary.getContents());
                diary.setContents(contents);
                diary.save();
            }
            if(intent.getAction().equals("insert")){
                String dates = intent.getStringExtra("dates");
                String contents = intent.getStringExtra("contents");
                Diary diary = new Diary(dates,contents);
                diary.save();
            }
        }else{
            Logger.D("intent ==  null");
        }
    }
    private void checkDataSupport(){
        DataControl.getConnection();
        DataControl.checkDiaryExist();
    }
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


}