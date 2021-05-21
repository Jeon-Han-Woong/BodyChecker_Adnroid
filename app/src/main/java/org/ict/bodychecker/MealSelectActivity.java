package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ict.bodychecker.ValueObject.MealVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MealSelectActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    String date;
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    ListView mealSelected, mealUnSelected;
    Button cancelBtn, selectBtn;
    View dlgView;
    EditText msNameInsert, msCalInsert;

    String[] str = {"밥", "삼겹살", "레드콤보", "배추김치", "계란후라이", "갈비찜", "카레라이스", "김치찌개"};
    int[] kcals = {313, 670, 1939, 60, 105, 163, 633, 57};
    int kcal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        mealSelected = (ListView) findViewById(R.id.mealSelected);
        mealUnSelected = (ListView) findViewById(R.id.mealUnSelected);
        cancelBtn = (Button) findViewById(R.id.msCancelBtn);
        selectBtn = (Button) findViewById(R.id.selectBtn);
        dlgView = (View) View.inflate(MealSelectActivity.this, R.layout.ms_dialog, null);

        ArrayList<String> selectList = new ArrayList<>();
        ArrayList<String> unselectList = new ArrayList<>();
        ArrayList<Integer> skcallist = new ArrayList<>();
        ArrayList<Integer> uskcallist = new ArrayList<>();

        selectList.add("직접 추가하기");

        for(int i=0; i<str.length; i++) {
            unselectList.add(str[i]);
            uskcallist.add(kcals[i]);
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
                kcal += uskcallist.get(i);
                skcallist.add(uskcallist.get(i));
                uskcallist.remove(uskcallist.get(i));

                sAdapter.notifyDataSetChanged();
                usAdapter.notifyDataSetChanged();
            }
        });//mealUnSelctedOnItemClick

        mealSelected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    String food = selectList.get(i);
                    unselectList.add(food);
                    selectList.remove(food);
                    kcal -= skcallist.get(i-1);
                    uskcallist.add(skcallist.get(i-1));
                    skcallist.remove(skcallist.get(i-1));
                } else if(i == 0) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MealSelectActivity.this);
                    dlg.setTitle("음식 정보 입력");
                    dlg.setView(dlgView);

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            msNameInsert = (EditText) dlgView.findViewById(R.id.msNameInsert);
                            msCalInsert = (EditText) dlgView.findViewById(R.id.msCalInsert);

                            String food = msNameInsert.getText().toString();
                            int Cal = Integer.parseInt(msCalInsert.getText().toString());
                            selectList.add(food);
                            kcal += Cal;
                            skcallist.add(Cal);
                        }
                    });//setPositiveButton

                    dlg.setNegativeButton("취소", null);//setNegativeButton

                    dlg.show();
                }//else if

                sAdapter.notifyDataSetChanged();
                usAdapter.notifyDataSetChanged();
            }//onItemClick
        });//mealSelctedOnItemClick

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                selectList.remove("직접 추가하기");

                Intent gIntent = getIntent();
                String time = gIntent.getStringExtra("time");
                date = gIntent.getStringExtra("date");

                Intent sintent = new Intent();
                sintent.putExtra("selected", selectList);
                sintent.putExtra("kcal", kcal);

                switch(time) {
                    case "breakfast"://아침
                        setResult(0, sintent);
                        removeDB(date, time);

                        for(int i=0; i<selectList.size(); i++) {
                            MealVO vo = new MealVO();
                            vo.setFdate(date);
                            vo.setFname(selectList.get(i));
                            vo.setFkcal(skcallist.get(i));
                            vo.setFtime("breakfast");
                            retrofitInterface.addFoods(vo).enqueue(new Callback<MealVO>() {
                                @Override
                                public void onResponse(Call<MealVO> call, Response<MealVO> response) {
                                    Log.d("fname", vo.getFname());
                                }

                                @Override
                                public void onFailure(Call<MealVO> call, Throwable t) {
                                    Log.d("fail", "fail");
                                }
                            });
                        }

                        break;
                    case "lunch"://점심
                        setResult(1, sintent);
                        break;
                    case "dinner"://저녁
                        setResult(2, sintent);
                        break;
                    case "disert"://간식
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//툴바

    private void removeDB(String date, String time) {

        switch(time) {
            case "breakfast":
                retrofitInterface.removeFoods(date, time);
                break;
        }
    }
}
