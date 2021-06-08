package com.example.wxdiary.data;

import com.example.wxdiary.bean.Diary;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据控制类
 */
public class DataControl {
    // 进行与数据库连通
    public static void getConnection() {
        LitePal.getDatabase();
    }
    // 第一次初始化处理，如果没有建立数据库，则建立并创建一下内容
    public static void checkDiaryExist() {
        List<Diary> diaries =  DataSupport.findAll(Diary.class);
        if(diaries.size() == 0){
            Diary diary1 = new Diary("2020-2-1","hellohhhhhhhhh");
            Diary diary2 = new Diary("2020-2-2","hello");
            Diary diary3 = new Diary("2020-2-3","hello");
            Diary diary4 = new Diary("2020-2-4","hello");
            diary1.save();
            diary2.save();
            diary3.save();
            diary4.save();

        }
    }

}
