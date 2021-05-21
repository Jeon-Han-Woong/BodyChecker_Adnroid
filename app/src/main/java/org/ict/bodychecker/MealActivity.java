package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MealActivity extends AppCompatActivity {
    final String TIME = "T00:00:00";
    String str;

    LocalDateTime date = LocalDateTime.now();
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter btnFormat = DateTimeFormatter.ofPattern("MM/dd");

    View meal_calendar;
    CircleProgressBar circlebar;
    LinearLayout bfll, lcll, dnll, dsll;
    TextView breakfast, lunch, dinner, disert, all;
    TextView breakfastCal, lunchCal, dinnerCal, disertCal;
    MaterialButton moreDaysAgoBtn, sixDaysAgoBtn, fiveDaysAgoBtn, fourDaysAgoBtn, threeDaysAgoBtn, twoDaysAgoBtn, oneDaysAgoBtn, todayBtn;
    DatePicker mealDatePick;

    float rdi = ((168-100) * 0.9f * 30);
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        meal_calendar = (View) View.inflate(MealActivity.this, R.layout.meal_calendar, null);

        circlebar = (CircleProgressBar) findViewById(R.id.circlebar);

        bfll = (LinearLayout) findViewById(R.id.bfll);
        lcll = (LinearLayout) findViewById(R.id.lcll);
        dnll = (LinearLayout) findViewById(R.id.dnll);
        dsll = (LinearLayout) findViewById(R.id.dsll);

        breakfast = (TextView) findViewById(R.id.breakfast);
        lunch = (TextView) findViewById(R.id.lunch);
        dinner = (TextView) findViewById(R.id.dinner);
        disert = (TextView) findViewById(R.id.disert);
        breakfastCal = (TextView) findViewById(R.id.breakfastCal);
        lunchCal = (TextView) findViewById(R.id.lunchCal);
        dinnerCal = (TextView) findViewById(R.id.dinnerCal);
        disertCal = (TextView) findViewById(R.id.disertCal);
        all = (TextView) findViewById(R.id.all);

        moreDaysAgoBtn = (MaterialButton) findViewById(R.id.moreDaysAgoBtn);
        sixDaysAgoBtn = (MaterialButton) findViewById(R.id.sixDaysAgoBtn);
        fiveDaysAgoBtn = (MaterialButton) findViewById(R.id.fiveDaysAgoBtn);
        fourDaysAgoBtn = (MaterialButton) findViewById(R.id.fourDaysAgoBtn);
        threeDaysAgoBtn = (MaterialButton) findViewById(R.id.threeDaysAgoBtn);
        twoDaysAgoBtn = (MaterialButton) findViewById(R.id.twoDaysAgoBtn);
        oneDaysAgoBtn = (MaterialButton) findViewById(R.id.oneDaysAgoBtn);
        todayBtn = (MaterialButton) findViewById(R.id.todayBtn);

        todayBtn.setText(setBtnDate(0));
        oneDaysAgoBtn.setText(setBtnDate(-1));
        twoDaysAgoBtn.setText(setBtnDate(-2));
        threeDaysAgoBtn.setText(setBtnDate(-3));
        fourDaysAgoBtn.setText(setBtnDate(-4));
        fiveDaysAgoBtn.setText(setBtnDate(-5));
        sixDaysAgoBtn.setText(setBtnDate(-6));

        circlebar.setProgress(progress);
        all.setText("일일 권장량 " + String.valueOf((int)rdi) + "kcal 중 " + String.valueOf(progress) + "kcal 섭취");

//======================================== onclick 이벤트 =========================================
        bfll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "breakfast");
                intent.putExtra("date", dbFormat.format(date));
                startActivityForResult(intent, 200);
            }
        });//bfll.onclick

        lcll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "lunch");
                intent.putExtra("date", dbFormat.format(date));
                startActivityForResult(intent, 200);
            }
        });//lcll.onclick

        dnll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "dinner");
                intent.putExtra("date", dbFormat.format(date));
                startActivityForResult(intent, 200);
            }
        });//dnll.onclick

        dsll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "disert");
                intent.putExtra("date", dbFormat.format(date));
                startActivityForResult(intent, 200);
            }
        });//dsll.onclick

// 하단 날짜 버튼
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = LocalDateTime.now();
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }
        });//today.onclick

        oneDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = date.plusDays(-1);
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }
        });//oneDayAgo.onclick

        twoDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = date.plusDays(-2);
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }
        });//oneDayAgo.onclick

        threeDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = date.plusDays(-3);
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }
        });//oneDayAgo.onclick

        fourDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = date.plusDays(-4);
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }
        });//oneDayAgo.onclick

        fiveDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = date.plusDays(-5);
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }
        });//oneDayAgo.onclick

        sixDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = date.plusDays(-6);
                Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_SHORT).show();
            }
        });//oneDayAgo.onclick

        moreDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MealActivity.this);
                dlg.setTitle("날짜 선택");
                if(meal_calendar.getParent() != null) {
                    ((ViewGroup) meal_calendar.getParent()).removeView(meal_calendar);
                }
                dlg.setView(meal_calendar);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mealDatePick = (DatePicker) meal_calendar.findViewById(R.id.mealDatePick);

                        str = mealDatePick.getYear()+"-"+String.format("%02d", mealDatePick.getMonth()+1)+"-"+String.format("%02d", mealDatePick.getDayOfMonth())+TIME;
                        date = LocalDateTime.parse(str);

                        Toast.makeText(getApplicationContext(), String.valueOf(dbFormat.format(date)), Toast.LENGTH_SHORT).show();
                    }
                });//dlg.positive

                dlg.setNegativeButton("취소", null);

                dlg.show();
            }
        });//moreDaysAgo.onclick

    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==200) {
            ArrayList<String> list = new ArrayList<>();
            int kcal = 0;
            if(data!=null) {
                list = data.getStringArrayListExtra("selected");
                kcal = data.getIntExtra("kcal", 0);
            } else {
                list.add("");
            }

            String food = "";

            for(int i=1; i<list.size(); i++) {
                if(!food.equals("")) { food += ", "; }
                food += list.get(i);
            }

            switch(resultCode) {
                case -1: break;//취소 버튼
                case 0://아침
                    breakfast.setText(food);
                    breakfastCal.setText(String.valueOf(kcal));
                    break;
                case 1://점심
                    lunch.setText(food);
                    lunchCal.setText(String.valueOf(kcal));
                    break;
                case 2://저녁
                    dinner.setText(food);
                    dinnerCal.setText(String.valueOf(kcal));
                    break;
                case 3://간식
                    disert.setText(food);
                    disertCal.setText(String.valueOf(kcal));
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                    break;
            }//switch
        } else {
            Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
        }//else

        progress = Integer.parseInt(breakfastCal.getText().toString())
                + Integer.parseInt(lunchCal.getText().toString())
                + Integer.parseInt(dinnerCal.getText().toString())
                + Integer.parseInt(disertCal.getText().toString());

        circlebar.setProgress((int)(progress/rdi*100));
        all.setText("일일 권장량 " + (int)rdi + "kcal 중 " + progress + "kcal 섭취");

        super.onActivityResult(requestCode, resultCode, data);
    }//onActivityResult

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

    public String setBtnDate(int n) {
        return btnFormat.format(date.plusDays(n));
    }//getDate
}
