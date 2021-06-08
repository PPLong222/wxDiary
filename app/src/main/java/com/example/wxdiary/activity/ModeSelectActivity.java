package com.example.wxdiary.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.wxdiary.R;
import com.example.wxdiary.activity.bylitepal.SelectActivity;
import com.example.wxdiary.activity.byservice.SelectActivityByService;

public class ModeSelectActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout ly_Normal;
    private RelativeLayout ly_Service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_select);

        ly_Normal = findViewById(R.id.panel_litepal);
        ly_Service = findViewById(R.id.panel_service);
        ly_Normal.setOnClickListener(this);
        ly_Service.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.panel_litepal)
            startActivity(new Intent(ModeSelectActivity.this, SelectActivity.class));
        if(v.getId() == R.id.panel_service)
            startActivity(new Intent(ModeSelectActivity.this, SelectActivityByService.class));
    }
}