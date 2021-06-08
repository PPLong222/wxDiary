package com.example.wxdiary.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wxdiary.R;
import com.example.wxdiary.bean.Diary;

import java.util.List;

public class EditAdapter extends ArrayAdapter<Diary> {
    private int resourceId;

    public EditAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Diary> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Diary diary = getItem(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        TextView textView = view.findViewById(R.id.edit_text);
        textView.setText(""+diary.getDates());
        return view;
    }
}
