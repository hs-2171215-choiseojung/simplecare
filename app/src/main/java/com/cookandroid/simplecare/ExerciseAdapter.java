package com.cookandroid.simplecare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exerciseList; // 운동 목록 데이터

    // 생성자
    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(itemView);
    }

    // ViewHolder에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.nameTextView.setText(exercise.getName());
        holder.caloriesTextView.setText(String.valueOf(exercise.getCalories()));
    }

    // 항목 개수 반환
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    // ViewHolder 클래스
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView; // 운동 이름 텍스트뷰
        public TextView caloriesTextView; // 칼로리 텍스트뷰

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_textview);
            caloriesTextView = itemView.findViewById(R.id.calories_textview);
        }
    }
}
