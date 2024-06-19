package com.cookandroid.simplecare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private List<MealItem> foodItems; // 음식 항목 목록

    // ViewHolder 클래스 정의
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDesc; // 음식 설명 텍스트뷰
        public TextView textViewNutrCont1; // 영양소 정보 텍스트뷰
        public CheckBox checkBoxSelected; // 선택 여부 체크박스

        public FoodViewHolder(View itemView) {
            super(itemView);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            textViewNutrCont1 = itemView.findViewById(R.id.textViewNutrCont1);
            checkBoxSelected = itemView.findViewById(R.id.checkBoxSelected);
        }
    }

    // 생성자
    public FoodListAdapter(List<MealItem> foodItems) {
        this.foodItems = foodItems;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
        return new FoodViewHolder(view);
    }

    // ViewHolder 를 통해 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        MealItem foodItem = foodItems.get(position);
        holder.textViewDesc.setText(foodItem.getDescKor());
        holder.textViewNutrCont1.setText(foodItem.getNutrCont1());
        holder.checkBoxSelected.setChecked(foodItem.isSelected());

        // CheckBox 클릭 이벤트 처리
        holder.checkBoxSelected.setOnClickListener(v -> {
            boolean isChecked = holder.checkBoxSelected.isChecked();
            foodItem.setSelected(isChecked);
            // 여기서 선택 여부에 따른 추가 로직을 구현할 수 있음
        });
    }

    // 데이터셋의 크기 반환
    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    // 데이터 갱신
    public void updateItems(List<MealItem> newItems) {
        foodItems.clear();
        foodItems.addAll(newItems);
        notifyDataSetChanged();
    }
}
