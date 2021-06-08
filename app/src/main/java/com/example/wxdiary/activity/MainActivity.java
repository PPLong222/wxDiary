package com.example.wxdiary.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxdiary.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etPassword;
    private Button btn_Login;
    // 密码为1234
    private String password = "1234";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findv();
    }

    private void findv() {
        etPassword = (EditText)findViewById(R.id.etPassword);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (etPassword.getText().toString().equals(password)) {
            startActivity(new Intent(MainActivity.this,ModeSelectActivity.class));
        }else{
            // 输出错误信息
            Toast.makeText(this, "密码错误哦~~", Toast.LENGTH_SHORT).show();
        }
  }



}