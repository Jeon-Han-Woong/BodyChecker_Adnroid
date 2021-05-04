package org.ict.bodychecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MealSelectActivity extends AppCompatActivity {

    ListView mealSelected, mealUnSelected;
    Button cancelBtn, selectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_select);

        mealSelected = (ListView) findViewById(R.id.mealSelected);
        mealUnSelected = (ListView) findViewById(R.id.mealUnSelected);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        selectBtn = (Button) findViewById(R.id.selectBtn);

        String[] str = {"밥", "비빔냉면", "레드콤보"};
        ArrayList<String> selectList = new ArrayList<>();
        ArrayList<String> unselectList = new ArrayList<>();

        for(int i=0; i<str.length; i++) {
            unselectList.add(str[i]);
        }//for

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectList);
        ArrayAdapter<String> usAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unselectList);

        mealSelected.setAdapter(sAdapter);
        mealUnSelected.setAdapter(usAdapter);

        mealUnSelected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String food = unselectList.get(i);
                selectList.add(food);
                unselectList.remove(food);

                sAdapter.notifyDataSetChanged();
                usAdapter.notifyDataSetChanged();
            }
        });//mealUnSelctedOnItemClick

        mealSelected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String food = selectList.get(i);
                unselectList.add(food);
                selectList.remove(food);

                sAdapter.notifyDataSetChanged();
                usAdapter.notifyDataSetChanged();
            }
        });//mealSelctedOnItemClick

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = getIntent();
                String meal = gIntent.getStringExtra("meal");

                Intent sintent = new Intent();
                sintent.putExtra("selected", selectList);

                switch(meal) {
                    case "bf"://아침
                        setResult(0, sintent);
                        break;
                    case "lc"://점심
                        setResult(1, sintent);
                        break;
                    case "dn"://저녁
                        setResult(2, sintent);
                        break;
                    case "ds"://간식
                        setResult(3, sintent);
                        break;
                }//switch

                finish();
            }
        });//selectedBtnOnClick

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(-1);

                finish();
            }
        });//취소버튼
    }//onCreate
}
