package com.example.wxdiary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wxdiary.R;
import com.example.wxdiary.bean.Diary;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    private List<Diary> list;

    public DiaryAdapter(List<Diary> list) {
        this.list = list;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_diary_show, parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dates.setText(""+list.get(position).getDates());
        holder.contents.setText(""+list.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dates;
        TextView contents;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dates = itemView.findViewById(R.id.text_showDates);
            contents = itemView.findViewById(R.id.text_showContents);
        }
    }

}
