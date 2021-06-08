package com.example.wxdiary.activity.byservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wxdiary.R;
import com.example.wxdiary.adapter.DiaryAdapter;
import com.example.wxdiary.bean.Diary;

import java.util.List;

/**
 * 通过Recyclerview显示所有日记，可通过左右滑动来查看
 */
public class SingleDiaryShowActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<Diary> diaryList;
    private int initIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_check);

        getFromIntent();

        setUI();

    }

    private void getFromIntent() {
        Intent intent = getIntent();
        initIndex = intent.getIntExtra("index", 0);
        diaryList = (List<Diary>) intent.getSerializableExtra("diaryList");
    }

    private void setUI(){

        recyclerView = findViewById(R.id.check_recyclerview);
        DiaryAdapter adapter = new DiaryAdapter(diaryList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerView.scrollToPosition(initIndex);
        // pagesnaphelper用于让recyclerview页面能够实现整页滑动
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }
}