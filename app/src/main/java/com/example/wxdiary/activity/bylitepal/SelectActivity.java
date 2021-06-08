package com.example.wxdiary.activity.bylitepal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wxdiary.activity.byservice.SingleDiaryShowActivity;
import com.example.wxdiary.helper.Logger;
import com.example.wxdiary.R;
import com.example.wxdiary.adapter.EditAdapter;
import com.example.wxdiary.bean.Diary;
import com.example.wxdiary.data.DataControl;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 选择是编辑还是查看的界面
 */
public class SelectActivity extends AppCompatActivity {
    private RelativeLayout btn_List;
    private static final int CHECK_ON_BACK = 1;
    private static final int EDIT_ON_BACK = 2;
    private RelativeLayout btn_Edit;
    private List<Diary> diaryList ;
    private AlertDialog checkDialog;
    private AlertDialog editDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        setUI();
        checkDataSupport();
    }
    private void checkDataSupport(){
        DataControl.getConnection();
        DataControl.checkDiaryExist();
    }
    private void setUI(){
        btn_List = findViewById(R.id.btn_List);
        btn_Edit = findViewById(R.id.btn_Edit);

        btn_List.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createListDialog();
            }
        });
        btn_Edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditDialog();
            }
        });

    }
    // 创建查看的临时dialog
    private void createListDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        diaryList = DataSupport.findAll(Diary.class);
        sortDiary(diaryList);
        ArrayList<String> dates = new ArrayList<>();
        for (int i = 0; i < diaryList.size(); i++) {
            dates.add(diaryList.get(i).getDates());
        }
        // 加载查看界面panel的layout
        View view = LayoutInflater.from(this).inflate(R.layout.date_select_panel, null);
        ab.setTitle("选择查看的内容");
        ListView listView = view.findViewById(R.id.dates_listview);
        Button btnAdd = view.findViewById(R.id.btn_AddDiary);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SelectActivity.this, readWrite.class), CHECK_ON_BACK);
            }
        });
        // 进入具体的查看页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectActivity.this, SingleDiaryShowActivity.class);
                intent.putExtra("diaryList", (Serializable) diaryList);
                intent.putExtra("index",position);
                startActivityForResult(intent, CHECK_ON_BACK);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectActivity.this, android.R.layout.simple_list_item_1, dates);
        listView.setAdapter(adapter);
        ab.setView(view);
        checkDialog = ab.create();
        checkDialog.show();
    }
    // 对diarylist按照日期排序，这样能够依照日期顺序对diary进行排序
    private void sortDiary(List<Diary> list){
        Collections.sort(list, new Comparator<Diary>() {
            // 通过比较年-月-日大小来区分String的日期大小
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
    // 创建编辑页面的dialog
    private void createEditDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("选择要删除的日记");
        diaryList = DataSupport.findAll(Diary.class);
        sortDiary(diaryList);
        View view = LayoutInflater.from(this).inflate(R.layout.diary_edit_panel, null);
        ListView listView = view.findViewById(R.id.edit_listview);
        // 创建自己构建的Adapter
        EditAdapter editAdapter = new EditAdapter(SelectActivity.this, R.layout.single_item, diaryList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectActivity.this, EditActivity.class);
                intent.putExtra("date", diaryList.get(position).getDates());
                intent.putExtra("content", diaryList.get(position).getContents());
                startActivityForResult(intent, EDIT_ON_BACK);
            }
        });
        listView.setAdapter(editAdapter);
        ab.setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < diaryList.size(); i++) {
                    // 遍历每个子View 查看checkbox是否被删除
                    View inView = (View) listView.getChildAt(i);
                    CheckBox c = inView.findViewById(R.id.edit_heckbox);
                    if(c.isChecked()){
                        Diary diary = diaryList.get(i);
                        Diary.deleteAll(Diary.class,"dates = ?",diary.getDates());
                        Toast.makeText(SelectActivity.this, "成功删除！", Toast.LENGTH_SHORT).show();
                        diaryList.remove(i);
                        editDialog.dismiss();
                    }

                }
            }
        });
        ab.setView(view);
        editDialog = ab.create();
        editDialog.show();
    }
    // activity从后一个返回时对dialog进行关闭
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