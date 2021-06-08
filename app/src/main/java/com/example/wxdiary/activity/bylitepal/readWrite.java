package com.example.wxdiary.activity.bylitepal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wxdiary.R;
import com.example.wxdiary.bean.Diary;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;
/**
    用于“创建页面功能”，用于进行日记的创建、或者更新存在的日记
 */
public class readWrite extends AppCompatActivity implements View.OnClickListener{

    private EditText etTitle,etReadWrite;
    private Button btClear,btSave;
    private ImageView img_Calendar;
    private DatePickerDialog pickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_write);

        findv();
        bindButton();

        Intent it = this.getIntent();
        etTitle.setText(it.getStringExtra("title"));
        etReadWrite.setText(it.getStringExtra("content"));

    }

    private void findv() {
        etReadWrite = (EditText)findViewById(R.id.etReadWrite);
        etTitle = (EditText)findViewById(R.id.etTitle);
        img_Calendar = findViewById(R.id.icon_calendar);
        btClear = (Button)findViewById(R.id.btClear);
        btSave = (Button)findViewById(R.id.btSave);
    }

    private void bindButton() {
        btClear.setOnClickListener(this);
        btSave.setOnClickListener(this);
        img_Calendar.setOnClickListener(this);
    }
    private void checkFormatAndSave() {
        String mTitle = etTitle.getText().toString();
        String mContent = etReadWrite.getText().toString();
        // 确保date和content内容不为空
        if(mTitle == null || mTitle.equals("") || mContent.equals("") || mContent == null){
            Toast.makeText(this, "日期和内容输入不能为空哦", Toast.LENGTH_SHORT).show();
            return ;
        }
        // 查找日记
        List<Diary> res = DataSupport.where("dates = ?", mTitle).find(Diary.class);
        // 若找到了相同日期的日记
        if(res.size() != 0){
            // 生产dialog提示框
            AlertDialog.Builder builder = new AlertDialog.Builder(readWrite.this);
            builder.setIcon(R.drawable.ic_launcher_foreground);
            builder.setTitle("提示");
            builder.setMessage("发现了相同日期的日记，需要替换吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /// TODO : try catch exception if not find
                    Diary diary = res.get(0);
                    diary.setContents(mContent);
                    diary.save();
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else{
            // 未找到相同日期日记，则直接保存新日记
            Diary diary = new Diary(mTitle, mContent);
            diary.save();
            Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btClear) {
            etTitle.setText("");
            etReadWrite.setText("");
        }
        if (v.getId() == R.id.btSave) {
            checkFormatAndSave();
        }
        if(v.getId() == R.id.icon_calendar) {
            createTimePickDialog();
        }
    }
    // 生产TimepickDialog 用于选择时间
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createTimePickDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 选择后的回调处理
                etTitle.setText(""+year+"-"+(month+1)+"-"+dayOfMonth);
            }
        };
        pickerDialog = new DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }


}