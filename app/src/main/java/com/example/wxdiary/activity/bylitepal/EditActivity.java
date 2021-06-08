package com.example.wxdiary.activity.bylitepal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxdiary.R;
import com.example.wxdiary.bean.Diary;
/**
  编辑页面，进行简单的编辑操作
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnConfirm;
    private Button btnCancel;
    private Button btnClear;
    private TextView date;
    private EditText content;
    private String oldContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setUI();
    }

    private void setUI(){
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        btnClear = findViewById(R.id.btn_clear);
        date = findViewById(R.id.edit_text_date);
        content = findViewById(R.id.edit_edittext);
        //得到intent传入的date 和 content值
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
           // 确认内容不为空后进行更新
           if(checkContentsFormat()) {
               Diary diary = new Diary(getIntent().getStringExtra("date"), content.getText().toString());
               diary.updateAll("contents = ?",oldContent);
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
    // 确认content的内容不为空
    private boolean checkContentsFormat() {
        if(content.getText().toString().equals("")){
            Toast.makeText(this, "输入的日记不能为空哦~~", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}