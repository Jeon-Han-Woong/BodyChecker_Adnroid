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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealSelectActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    ArrayList<String> selectList = new ArrayList<>();
    ArrayList<String> unselectList = new ArrayList<>();
    ArrayList<Integer> selectKcalList = new ArrayList<>();
    ArrayList<Integer> unselectKcalList = new ArrayList<>();

    Intent intent;
    String date, time;
    int mno = 0;

    ListView mealSelected, mealUnSelected;
    Button cancelBtn, selectBtn;
    View dlgView;
    EditText msNameInsert, msCalInsert;

    String[] str = {"밥", "삼겹살", "레드콤보", "배추김치", "계란후라이", "갈비찜", "카레라이스", "김치찌개"};
    int[] kcals = {313, 670, 1939, 60, 105, 163, 633, 57};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        intent = getIntent();
        mno = intent.getIntExtra("mno", 0);

        mealSelected = (ListView) findViewById(R.id.mealSelected);
        mealUnSelected = (ListView) findViewById(R.id.mealUnSelected);
        cancelBtn = (Button) findViewById(R.id.msCancelBtn);
        selectBtn = (Button) findViewById(R.id.selectBtn);
        dlgView = (View) View.inflate(MealSelectActivity.this, R.layout.ms_dialog, null);


        selectList.add("직접 추가하기");

        for(int i=0; i<str.length; i++) {
            unselectList.add(str[i]);
            unselectKcalList.add(kcals[i]);
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
                selectKcalList.add(unselectKcalList.get(i));
                unselectKcalList.remove(unselectKcalList.get(i));

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
                    unselectKcalList.add(selectKcalList.get(i-1));
                    selectKcalList.remove(selectKcalList.get(i-1));
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
                            selectKcalList.add(Cal);
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

                Intent getIntent = getIntent();
                time = getIntent.getStringExtra("time");
                date = getIntent.getStringExtra("date");

                Intent setIntent = new Intent(getApplicationContext(), MealActivity.class);
                setIntent.putExtra("date", date);

                removeDB(date, time, mno);
                addDB(date, time, mno);
                if(selectList.isEmpty())
                try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                else
                try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

                finish();
            }
        });//selectedBtnOnClick

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setIntent = new Intent(getApplicationContext(), MealActivity.class);
                setIntent.putExtra("date", date);

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

    private void addDB(String date, String time, int mno) {
        for(int i=0; i<selectList.size(); i++) {
            MealVO vo = new MealVO();
            vo.setMno(mno);
            vo.setFdate(date);
            vo.setFname(selectList.get(i));
            vo.setFkcal(selectKcalList.get(i));
            vo.setFtime(time);
            retrofitInterface.addFoods(vo).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) { Log.d("addSuccess", vo.getFname()); }
                @Override
                public void onFailure(Call<String> call, Throwable t) { t.printStackTrace(); }
            });
        }//for
    }//addDB

    private void removeDB(String date, String time, int mno) {
        retrofitInterface.removeFoods(date, time, mno).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) { Log.d("removeSuccess", "삭제성공"); }

            @Override
            public void onFailure(Call<String> call, Throwable t) { t.printStackTrace(); }
        });
    }//removeDB

}//class
