package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MealActivity extends AppCompatActivity {

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

    View meal_calendar;
    CircleProgressBar circlebar;
    LinearLayout bfll, lcll, dnll, dsll;
    TextView breakfast, lunch, dinner, disert, all;
    TextView breakfastCal, lunchCal, dinnerCal, disertCal;
    MaterialButton moreDaysAgo, sixDaysAgo, fiveDaysAgo, fourDaysAgo, threeDaysAgo, twoDaysAgo, oneDaysAgo, today;
    TextView sixDaysAgoTV, fiveDaysAgoTV, fourDaysAgoTV, threeDaysAgoTV, twoDaysAgoTV, oneDaysAgoTV, todayTV;
    TextView[] daysArr = {todayTV, oneDaysAgoTV, twoDaysAgoTV, threeDaysAgoTV, fourDaysAgoTV, fiveDaysAgoTV, sixDaysAgoTV};
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

        moreDaysAgo = (MaterialButton) findViewById(R.id.moreDaysAgo);
        sixDaysAgo = (MaterialButton) findViewById(R.id.sixDaysAgo);
        fiveDaysAgo = (MaterialButton) findViewById(R.id.fiveDaysAgo);
        fourDaysAgo = (MaterialButton) findViewById(R.id.fourDaysAgo);
        threeDaysAgo = (MaterialButton) findViewById(R.id.threeDaysAgo);
        twoDaysAgo = (MaterialButton) findViewById(R.id.twoDaysAgo);
        oneDaysAgo = (MaterialButton) findViewById(R.id.oneDaysAgo);
        today = (MaterialButton) findViewById(R.id.today);

        sixDaysAgoTV = (TextView) findViewById(R.id.sixDaysAgoTV);
        fiveDaysAgoTV = (TextView) findViewById(R.id.fiveDaysAgoTV);
        fourDaysAgoTV = (TextView) findViewById(R.id.fourDaysAgoTV);
        threeDaysAgoTV = (TextView) findViewById(R.id.threeDaysAgoTV);
        twoDaysAgoTV = (TextView) findViewById(R.id.twoDaysAgoTV);
        oneDaysAgoTV = (TextView) findViewById(R.id.oneDaysAgoTV);
        todayTV = (TextView) findViewById(R.id.todayTV);

        todayTV.setText(getDate(0, cal));
        oneDaysAgoTV.setText(getDate(-1, cal));
        twoDaysAgoTV.setText(getDate(-2, cal));
        threeDaysAgoTV.setText(getDate(-3, cal));
        fourDaysAgoTV.setText(getDate(-4, cal));
        fiveDaysAgoTV.setText(getDate(-5, cal));
        sixDaysAgoTV.setText(getDate(-6, cal));

        circlebar.setProgress(progress);
        all.setText("일일 권장량 " + String.valueOf((int)rdi) + "kcal 중 " + String.valueOf(progress) + "kcal 섭취");
//======================================== onclick 이벤트 =========================================

        bfll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "bf");
                startActivityForResult(intent, 200);
            }
        });//bfll.onclick

        lcll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "lc");
                startActivityForResult(intent, 200);
            }
        });//lcll.onclick

        dnll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "dn");
                startActivityForResult(intent, 200);
            }
        });//dnll.onclick

        dsll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "ds");
                startActivityForResult(intent, 200);
            }
        });//dsll.onclick

        moreDaysAgo.setOnClickListener(new View.OnClickListener() {
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

                        int year = mealDatePick.getYear();
                        int month = mealDatePick.getMonth() + 1;
                        int day = mealDatePick.getDayOfMonth();

                        Toast.makeText(getApplicationContext(), year+"/"+month+"/"+day, Toast.LENGTH_SHORT).show();
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

    public String getDate(int n, Calendar cal) {
        cal.setTime(new Date());
        cal.add(Calendar.DATE, n);

        return sdf.format(cal.getTime());
    }//getDate
}
