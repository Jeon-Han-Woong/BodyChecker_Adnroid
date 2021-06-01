package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import org.ict.bodychecker.ValueObject.MealVO;
import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MealActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    LocalDateTime today = LocalDateTime.now();
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter btnFormat = DateTimeFormatter.ofPattern("MM/dd");

    String date = dbFormat.format(today);

    View meal_calendar;
    CircleProgressBar circlebar;
    LinearLayout bfll, lcll, dnll, dsll;
    TextView breakfastTV, lunchTV, dinnerTV, disertTV, all;
    TextView breakfastCal, lunchCal, dinnerCal, disertCal;
    MaterialButton moreDaysAgoBtn, sixDaysAgoBtn, fiveDaysAgoBtn, fourDaysAgoBtn, threeDaysAgoBtn, twoDaysAgoBtn, oneDaysAgoBtn, todayBtn;
    DatePicker mealDatePick;

    Intent intent;
    float rdi = 0;
    int progress = 0, mno = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        intent = getIntent();
        mno = intent.getIntExtra("mno", 0);

        meal_calendar = (View) View.inflate(MealActivity.this, R.layout.meal_calendar, null);

        circlebar = (CircleProgressBar) findViewById(R.id.circlebar);

        bfll = (LinearLayout) findViewById(R.id.bfll);
        lcll = (LinearLayout) findViewById(R.id.lcll);
        dnll = (LinearLayout) findViewById(R.id.dnll);
        dsll = (LinearLayout) findViewById(R.id.dsll);

        breakfastTV = (TextView) findViewById(R.id.breakfast);
        lunchTV = (TextView) findViewById(R.id.lunch);
        dinnerTV = (TextView) findViewById(R.id.dinner);
        disertTV = (TextView) findViewById(R.id.disert);
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

        getRDI(mno);
        try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        getMealList(date, mno);

//======================================== onclick 이벤트 =========================================
        bfll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "breakfast");
                intent.putExtra("date", date);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });//bfll.onclick

        lcll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "lunch");
                intent.putExtra("date", date);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });//lcll.onclick

        dnll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "dinner");
                intent.putExtra("date", date);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });//dnll.onclick

        dsll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("time", "disert");
                intent.putExtra("date", date);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });//dsll.onclick

// 하단 날짜 버튼
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                today = LocalDateTime.now();
                date = dbFormat.format(today);
                getMealList(date, mno);
            }
        });//today.onclick

        oneDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-1));
                getMealList(date, mno);
            }
        });//oneDayAgo.onclick

        twoDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-2));
                getMealList(date, mno);
            }
        });//oneDayAgo.onclick

        threeDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-3));
                getMealList(date, mno);
            }
        });//oneDayAgo.onclick

        fourDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-4));
                getMealList(date, mno);
            }
        });//oneDayAgo.onclick

        fiveDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-5));
                getMealList(date, mno);
            }
        });//oneDayAgo.onclick

        sixDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-6));
                getMealList(date, mno);
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

                        date = mealDatePick.getYear()+"-"+String.format("%02d", mealDatePick.getMonth()+1)+"-"+String.format("%02d", mealDatePick.getDayOfMonth());
                        getMealList(date, mno);
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
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            getMealList(date, mno);
        } else {
            Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
        }//else

        circlebar.setProgress((int)(progress/rdi*100));
        all.setText("일일 권장량 " + (int)rdi + "kcal 중 " + progress + "kcal 섭취");

        super.onActivityResult(requestCode, resultCode, data);
    }//onActivityResult

    private String setBtnDate(int n) {
        return btnFormat.format(today.plusDays(n));
    }//getDate

    private void getMealList(String date, int mno) {
        retrofitInterface.getDailyMeal(date, mno).enqueue(new Callback<List<MealVO>>() {
            String breakfast = null, lunch = null, dinner = null, disert = null;
            int breakfastKcal = 0, lunchKcal = 0, dinnerKcal = 0, disertKcal = 0;
            @Override
            public void onResponse(Call<List<MealVO>> call, Response<List<MealVO>> response) {
                progress = 0;
                Log.d("load", "불러오는중");
                response.body().forEach(mealVO -> {
                    switch(mealVO.getFtime()) {
                        case "breakfast":
                            if(breakfast != null) {breakfast += ", ";}
                            breakfast += mealVO.getFname();
                            breakfastKcal += mealVO.getFkcal();
                            break;
                        case "lunch":
                            if(lunch != null) lunch += ", ";
                            lunch += mealVO.getFname();
                            lunchKcal += mealVO.getFkcal();
                            break;
                        case "dinner":
                            if(dinner != null) dinner += ", ";
                            dinner += mealVO.getFname();
                            dinnerKcal += mealVO.getFkcal();
                            break;
                        case "disert":
                            if(disert != null) disert += ", ";
                            disert += mealVO.getFname();
                            disertKcal += mealVO.getFkcal();
                            break;
                    }//switch
                    progress += mealVO.getFkcal();
                });//forEach

                getRDI(mno);
                int per = (int)(progress/rdi*100);
                circlebar.setProgress(per);
                all.setText("일일 권장량 " + String.valueOf((int)rdi) + "kcal 중 " + String.valueOf(progress) + "kcal 섭취");

                if(breakfast!=null) breakfast = breakfast.replace("null", "");
                breakfastTV.setText(breakfast);
                if(lunch!=null) lunch = lunch.replace("null", "");
                lunchTV.setText(lunch);
                if(dinner!=null) dinner = dinner.replace("null", "");
                dinnerTV.setText(dinner);
                if(disert!=null) disert = disert.replace("null", "");
                disertTV.setText(disert);

                breakfastCal.setText(String.valueOf(breakfastKcal));
                lunchCal.setText(String.valueOf(lunchKcal));
                dinnerCal.setText(String.valueOf(dinnerKcal));
                disertCal.setText(String.valueOf(disertKcal));
            }//onResponse

            @Override
            public void onFailure(Call<List<MealVO>> call, Throwable t) {
                t.printStackTrace();
            }//onFailure
        });
    }//getMealList

    private void getRDI(int mno) {
        retrofitInterface.getInfo(mno).enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                MemberVO info = response.body();
                do{
                    rdi = ((info.getHeight()-100) * (info.getGender() == 1 ? 0.9f : 0.85f) * 30);
                }while(rdi == 0);
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }//getRDI

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected
}//class
