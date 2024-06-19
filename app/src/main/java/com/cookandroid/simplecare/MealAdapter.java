package com.cookandroid.simplecare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<MealItem> mealItemList;


    public void clearItems() {
        mealItemList.clear();
        notifyDataSetChanged(); // 데이터 변경을 RecyclerView에 알림
    }

    public MealAdapter(List<MealItem> mealItemList) {
        this.mealItemList = mealItemList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealItem currentItem = mealItemList.get(position);
        holder.descKorTextView.setText(currentItem.getDescKor());
        holder.checkBox.setChecked(currentItem.isSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentItem.setSelected(isChecked);
        });
    }


    // MealAdapter 클래스에서 getSelectedMealItems() 메서드 추가
    public List<MealItem> getSelectedMealItems() {
        List<MealItem> selectedItems = new ArrayList<>();
        for (MealItem item : mealItemList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }


    @Override
    public int getItemCount() {
        return mealItemList.size();
    }

    public List<MealItem> getMealItemList() {
        return mealItemList;
    }

    public List<MealItem> getSelectedItems() {
        List<MealItem> selectedItems = new ArrayList<>();
        for (MealItem item : mealItemList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }




    public void updateItems(List<MealItem> items) {
        mealItemList.clear(); // 기존 데이터 초기화
        mealItemList.addAll(items); // 새로운 데이터 추가
        notifyDataSetChanged(); // RecyclerView 갱신
    }


    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView descKorTextView;
        CheckBox checkBox;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            descKorTextView = itemView.findViewById(R.id.desc_kor_textview);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
