package com.example.wxdiary.bean;

import android.database.sqlite.SQLiteOpenHelper;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Diary日记类，使用litepal存储，继承DataSupport类 实现Serializable接口，便于序列化进行传递
 */
public class Diary extends DataSupport implements Serializable {
    private String dates;
    private String contents;
    public Diary() {

    }

    public Diary(String dates, String contents) {
        this.dates = dates;
        this.contents = contents;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
