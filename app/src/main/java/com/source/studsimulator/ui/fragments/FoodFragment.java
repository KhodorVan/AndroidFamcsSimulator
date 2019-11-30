package com.source.studsimulator.ui.fragments;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.source.studsimulator.R;
import com.source.studsimulator.model.ActionObjects;
import com.source.studsimulator.model.entity.Food;
import com.source.studsimulator.model.entity.StudentActivity;
import com.source.studsimulator.ui.fragments.adapters.OneActiveButtonAdapter;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {

    public enum FOOD_BUTTONS {
        NEIGHBOUR, DOSHIK, STOLOVAYA, COOK, FASTFOOD, SUSHI, BURGERS;
    }

    private RecyclerView foodRv;
    private List<StudentActivity> food;
    private int activeButtonIndex = -1;

    private FoodFragment.OnFoodFragmentListener activityListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_fragment_activity, null);

        initializeFood();

        foodRv = view.findViewById(R.id.buttonsRecyclerView);
        foodRv.setLayoutManager(new LinearLayoutManager(getContext()));
        foodRv.setHasFixedSize(true);

        OneActiveButtonAdapter foodRvAdapter = new OneActiveButtonAdapter(food);
        foodRv.setAdapter(foodRvAdapter);
        foodRvAdapter.setIndexOfActivatedButton(activeButtonIndex);

        foodRvAdapter.setAdapterListener(position -> {
            int currentPosition = foodRvAdapter.getIndexOfActivatedButton();
            if (currentPosition != -1) {
                activityListener.unclickFoodButton((Food) food.get(currentPosition));
            }
            foodRvAdapter.setIndexOfActivatedButton(position);
            changeButtonActivity(position);
            foodRvAdapter.notifyDataSetChanged();
            if (currentPosition != position) {
                activityListener.clickOnFoodButton((Food) food.get(position));
            }
        });

        return view;
    }

    private void changeButtonActivity(int position) {
        activeButtonIndex = activeButtonIndex == position ? -1 : position;
    }
  
    public interface OnFoodFragmentListener {
        void clickOnFoodButton(Food food);
        void unclickFoodButton(Food food);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FoodFragment.OnFoodFragmentListener) {
            activityListener = (FoodFragment.OnFoodFragmentListener) context;
        }
    }

    private int getIndexOfButton(FOOD_BUTTONS BUTTON) {
        switch (BUTTON) {
            case NEIGHBOUR:
                return 0;
            case DOSHIK:
                return 1;
            case STOLOVAYA:
                return 2;
            case COOK:
                return 3;
            case FASTFOOD:
                return 4;
            case SUSHI:
                return 5;
            case BURGERS:
                return 6;
            default:
                return -1;
        }
    }
      
    private void initializeFood() {
        food = ActionObjects.getFoodList();
        for (StudentActivity it : food) {
            System.out.println(it.toString());
        }
    }
}
