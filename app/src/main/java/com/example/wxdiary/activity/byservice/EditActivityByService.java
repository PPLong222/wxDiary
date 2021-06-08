package com.example.wxdiary.activity.byservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxdiary.R;
import com.example.wxdiary.bean.Diary;
import com.example.wxdiary.service.MyIntentService;

/**
 * 通过IntentService方式编辑内容
 */
public class EditActivityByService extends AppCompatActivity implements View.OnClickListener{

    private Button btnConfirm;
    private Button btnCancel;
    private Button btnClear;
    private TextView date;
    private EditText content;
    private String oldContent;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setUI();
    }
    private void setUI(){
        handler = new Handler();
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        btnClear = findViewById(R.id.btn_clear);
        date = findViewById(R.id.edit_text_date);
        content = findViewById(R.id.edit_edittext);
        date.setText("" + getIntent().getStringExtra("date"));
        content.setText("" + getIntent().getStringExtra("content"));
        oldContent = content.getText().toString();
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_confirm){
            if(checkContentsFormat()) {
                Intent intent = new Intent(EditActivityByService.this, MyIntentService.class);
                intent.putExtra("dates", date.getText().toString());
                intent.putExtra("contents",content.getText().toString());
                intent.setAction("update");
                // 提交请求给service，注明intent的action类型
                startService(intent);
                finish();
            }
        }
        if(v.getId() == R.id.btn_cancel){
            finish();
        }
        if(v.getId() == R.id.btn_clear){
            content.setText("");
        }
    }
    private boolean checkContentsFormat() {
        if(content.getText().toString().equals("")){
            Toast.makeText(this, "输入的日记不能为空哦~~", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}