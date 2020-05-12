package com.example.ble;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//      The adapter is used to define each item in the Listview
public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder> {
    private List<Lecture> mListLecture;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View lectureView;
        TextView LectureCode;
        TextView LectureName;
        TextView LectureData;
        TextView LectureTime;
        public ViewHolder(View view) {
            super(view);
            lectureView=view;
            LectureCode = (TextView) view.findViewById(R.id.lecture_code);
            LectureName=(TextView) view.findViewById(R.id.lecture_name);
            LectureData=(TextView) view.findViewById(R.id.lecture_data);
            LectureTime=(TextView) view.findViewById(R.id.lecture_time);
        }
    }

    public LectureAdapter(List<Lecture> List) {
        mListLecture= List;
    }
// Pass lecture information into BLE activity
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecture, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.lectureView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Lecture lecture=mListLecture.get(position);
                Context context=v.getContext();
                Intent intent = new Intent(context,BLE_Activity.class);
                intent.putExtra("Lecture",lecture);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Lecture lecture = mListLecture.get(position);
        holder.LectureName.setText("Module Name: "+lecture.getName());
        holder.LectureCode.setText("Module Code: "+lecture.getCode());
        holder.LectureData.setText("Data: "+lecture.getData());
        holder.LectureTime.setText("Time: "+lecture.getTime());
    }

    @Override
    public int getItemCount() {
        return mListLecture.size();
    }
}
