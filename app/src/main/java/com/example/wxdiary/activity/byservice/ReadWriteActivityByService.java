package com.example.wxdiary.activity.byservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wxdiary.R;
import com.example.wxdiary.bean.Diary;
import com.example.wxdiary.helper.Logger;
import com.example.wxdiary.service.MyIntentService;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class ReadWriteActivityByService extends AppCompatActivity implements View.OnClickListener{
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logger.D("onPause");
        super.onPause();
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
        if(mTitle == null || mTitle.equals("") || mContent.equals("") || mContent == null){
            Toast.makeText(this, "日期和内容输入不能为空哦", Toast.LENGTH_SHORT).show();
            return ;
        }
        List<Diary> res = DataSupport.where("dates = ?", mTitle).find(Diary.class);
        if(res.size() != 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ReadWriteActivityByService.this);
            builder.setIcon(R.drawable.ic_launcher_foreground);
            builder.setTitle("提示");
            builder.setMessage("发现了相同日期的日记，需要替换吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /// TODO : try catch exception if not find
                    Diary diary = res.get(0);
                    Intent intent = new Intent(ReadWriteActivityByService.this, MyIntentService.class);
                    intent.putExtra("dates", diary.getDates());
                    intent.putExtra("contents",mContent);
                    intent.setAction("update");
                    // 提交更新请求给service
                    startService(intent);
                    // 延迟关闭，模拟service后台处理耗时工作
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },200);
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
            Intent intent = new Intent(ReadWriteActivityByService.this, MyIntentService.class);
            intent.putExtra("dates", mTitle);
            intent.putExtra("contents",mContent);
            intent.setAction("insert");
            // 提交插入的添加日记请求
            startService(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ReadWriteActivityByService.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            },200);
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createTimePickDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etTitle.setText(""+year+"-"+(month+1)+"-"+dayOfMonth);
            }
        };
        pickerDialog = new DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }


}
