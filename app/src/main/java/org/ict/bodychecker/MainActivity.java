package org.ict.bodychecker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.ict.bodychecker.ValueObject.DailyVO;
import org.ict.bodychecker.ValueObject.GoalVO;
import org.ict.bodychecker.ValueObject.MealVO;
import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    int mno = 0;

    private long backKeyPressedTime = 0;

    LocalDateTime today = LocalDateTime.now();
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String date = dbFormat.format(today);

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    SensorManager sensorManager;
    Sensor stepCountSensor;

    DrawerLayout drawerLayout;
    Context context = this;
    View drawerView;
    ListView listView;
    TextView hitext;
    TextView recentGoal, dDay;
    ProgressBar walkProgress, waterProgress;
    LinearLayout goGoalBtn, goExerciseBtn, goMealBtn, goProfileBtn;
    TextView drinkWater, nowWater, myWeight, myBMI;
    TextView myWalk;
    TextView reduceKcal;
    Button waterPlus, waterMinus;
    int temp_water = 0, rdi = 0, currentSteps = 0;

    TextView profileName, profileAge, profileHeight, profileWeight, profileBMIName;
//    profileBMI,
    TextView main_dayKcal, main_maxKcal;

    GoalVO goalDday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = getLayoutInflater();
        View navi_header = inflater.inflate(R.layout.navi_header, null);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        recentGoal = (TextView) findViewById(R.id.recentGoal);
        dDay = (TextView) findViewById(R.id.dDay);

        goExerciseBtn = (LinearLayout) findViewById(R.id.goExerciseBtn);
        goMealBtn = (LinearLayout) findViewById(R.id.goMealBtn);
        goGoalBtn = (LinearLayout) findViewById(R.id.goGoalBtn);
        goProfileBtn = (LinearLayout) findViewById(R.id.goProfileBtn);
        waterPlus = (Button) findViewById(R.id.waterPlus);
        waterMinus = (Button) findViewById(R.id.waterMinus);
        drinkWater = (TextView) findViewById(R.id.drinkWater);
        nowWater = (TextView) findViewById(R.id.nowWater);
        myWeight = (TextView) findViewById(R.id.myWeight);
        myBMI = (TextView) findViewById(R.id.myBMI);
        myWalk = (TextView) findViewById(R.id.myWalk);
        reduceKcal = (TextView) findViewById(R.id.reduceKcal);
        waterProgress = (ProgressBar) findViewById(R.id.waterProgress);

        profileName = (TextView) navi_header.findViewById(R.id.profileName);
        profileAge = (TextView) navi_header.findViewById(R.id.profileAge);
        profileHeight = (TextView) navi_header.findViewById(R.id.profileHeight);
        profileWeight = (TextView) navi_header.findViewById(R.id.profileWeight);
//        profileBMI = (TextView) navi_header.findViewById(R.id.profileBMI);
        profileBMIName = (TextView) navi_header.findViewById(R.id.profileBMIName);

        goGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });

        goProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });

        goExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });

        goMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealActivity.class);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });

        waterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterPlus(date, mno);
            }
        });

        waterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp_water > 0) {
                    waterMinus(date, mno);
                } else {
                    Toast.makeText(getApplicationContext(), "물 섭취량은 0보다 낮을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menubar);
        hitext = (TextView) findViewById(R.id.hitext);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id == R.id.myProfile) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("mno", mno);
                    startActivityForResult(intent, 200);
                } else if (id == R.id.myGoal) {
                    Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                    intent.putExtra("mno", mno);
                    startActivityForResult(intent, 200);
                } else if (id == R.id.notice) {
                    Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                    startActivityForResult(intent, 200);
                } else if (id == R.id.FAQ) {
                    Intent intent = new Intent(getApplicationContext(), FAQActivity.class);
                    startActivityForResult(intent, 200);
                } else if(id == R.id.logout) {
                    SharedPreferences autoLogin = getSharedPreferences("auto", MODE_PRIVATE);
                    SharedPreferences.Editor editor = autoLogin.edit();
                    editor.putInt("MNO", 0);
                    editor.commit();
                    mno = 0;
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, 200);
                }

                return false;
            }
        });//setNavigationItemSelectedListener
        navigationView.addHeaderView(navi_header);

        /*=================== DB정보 불러오기 ===================*/
        getProfileInfo(mno);

    }//onCreate

    public void onStart() {
        super.onStart();
        if(stepCountSensor != null) {
            sensorManager.registerListener( this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(e.values[0]==1.0f) {
                currentSteps++;
                myWalk.setText(currentSteps + " / 10000");
                walkProgress.setProgress(currentSteps);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*======================= 호출 메서드 =======================*/
    private void getDailyWater(String date, int mno) {
        retrofitInterface.getDailyWater(date, mno).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                Toast.makeText(MainActivity.this, "물" + response.body(), Toast.LENGTH_SHORT).show();

                Log.d("water", String.valueOf(response.body()));
                temp_water = response.body();
                setWaterState(temp_water);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("getDailyWater", "getDailyWater Fail");
                Log.d("getDailyWater", "Daily 테이블에 정보가 없어 발생할 가능성 높음");
                addDaily(mno);
//                t.printStackTrace();
            }
        });
    }//getDailyWater

    private void addDaily(int mno) {
        DailyVO dailyVO = new DailyVO();
        dailyVO.setMno(mno);
        dailyVO.setDdate(date);

        retrofitInterface.addDaily(dailyVO).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                getDailyWater(date, mno);
                Log.d("addDaily", "daily 테이블 생성 완료");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("addDaily", "addDaily Fail");
//                t.printStackTrace();
            }
        });
    }//addDaily

    private void waterPlus(String date, int mno) {
        retrofitInterface.plusWater(date, mno).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                Toast.makeText(MainActivity.this, "한 잔" + response.body(), Toast.LENGTH_SHORT).show();
                getDailyWater(date, mno);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }//waterPlus

    private void waterMinus(String date, int mno) {
        retrofitInterface.minusWater(date, mno).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                temp_water = response.body();
                setWaterState(temp_water);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }//waterMinus

    private void setWaterState(int water) {
        waterProgress.setProgress(water * 10);
        if (water < 5) {
            drinkWater.setTextColor(Color.RED);
            nowWater.setTextColor(Color.RED);
        } else if (water >= 5 && water < 10) {
            drinkWater.setTextColor(Color.parseColor("#FF5E00"));
            nowWater.setTextColor(Color.parseColor("#FF5E00"));
        } else if (water >= 10) {
            drinkWater.setTextColor(Color.parseColor("#189186"));
            nowWater.setTextColor(Color.parseColor("#189186"));
        }

        nowWater.setText((water * 200) + " ml");
        drinkWater.setText(water + "");
    }//waterState

    private void dailySumKcal(String date, int mno){
        retrofitInterface.getSumKcal(date, mno).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                reduceKcal.setText(String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("dailySumKcal", "dailySumKcal Fail");
                Log.d("dailySumKcal", "Exercise 테이블에 정보가 없어서 발생할 가능성 높음");
//                t.printStackTrace();
            }
        });
    }//dailySumKcal

    private void getProfileInfo(int mno) {
        Log.d("getProfileInfoStart", String.valueOf(mno));
        retrofitInterface.getInfo(mno).enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {

                MemberVO info = response.body();
                String name = info.getMname();
                String birthday = info.getBirthday();
                String height = String.valueOf(info.getHeight()).concat("cm");
                String weight = String.valueOf(info.getWeight()).concat("kg");
                int bmi = info.getBmi();

                profileName.setText(name);
                profileAge.setText(birthday);
                profileHeight.setText(height);
                profileWeight.setText(weight);
                myWeight.setText(weight);
//                profileBMI.setText(String.valueOf(bmi));
                if(bmi < 18.5) {
                    myBMI.setText("확찌자");
                    myBMI.setTextColor(Color.rgb(51, 102, 153));
                    profileBMIName.setText("확찌자 (" + bmi + ")");
                    profileBMIName.setTextColor(Color.rgb(51, 102, 153));
                } else if(18.5 < bmi && bmi < 23) {
                    myBMI.setText("안찐자");
                    myBMI.setTextColor(Color.rgb(121, 210, 121));
                    profileBMIName.setText("안찐자 (" + bmi + ")");
                    profileBMIName.setTextColor(Color.rgb(121, 210, 121));
                } else if(23 <= bmi && bmi < 25) {
                    myBMI.setText("좀찐자");
                    myBMI.setTextColor(Color.rgb(233, 105, 0));
                    profileBMIName.setText("좀찐자 (" + bmi + ")");
                    profileBMIName.setTextColor(Color.rgb(233, 105, 0));
                } else if(25 <= bmi && bmi < 28) {
                    myBMI.setText("확찐자");
                    myBMI.setTextColor(Color.rgb(192, 39, 34));
                    profileBMIName.setText("확찐자 (" + bmi + ")");
                    profileBMIName.setTextColor(Color.rgb(192, 39, 34));
                } else if(28 <= bmi) {
                    myBMI.setText("확!찐자");
                    myBMI.setTextColor(Color.rgb(209, 0, 0));
                    profileBMIName.setText("확!찐자 (" + bmi + ")");
                    profileBMIName.setTextColor(Color.rgb(209, 0, 0));
                }//else if

            }//onResponse

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                Log.d("getProfileInfo", "getProfileInfo Fail");
                Log.d("getProfileInfo", "Member 테이블에 정보가 없거나");
                Log.d("getProfileInfo", "로그인이 안돼있어 발생할 가능성 높음");
//                t.printStackTrace();

                /*================ 로그인페이지로 이동시키는 메서드 ================*/
                goLoginPage();
            }//onFailure
        });
    }//getProfileInfo

    private void goLoginPage() {
        startActivityForResult(new Intent(this, LoginActivity.class), 200);
    }//goLoginPage

    private void getMealInfo(String date, int mno) {
        retrofitInterface.getDailyMeal(date, mno).enqueue(new Callback<List<MealVO>>() {
            @Override
            public void onResponse(Call<List<MealVO>> call, Response<List<MealVO>> response) {
                main_dayKcal = (TextView) findViewById(R.id.main_dayKcal);
                main_maxKcal = (TextView) findViewById(R.id.main_maxKcal);

                int kcal = response.body().stream().mapToInt(MealVO::getFkcal).sum();
                main_dayKcal.setText(String.valueOf(kcal));
                main_maxKcal.setText("/ ".concat(String.valueOf(rdi)).concat("kcal"));
            }

            @Override
            public void onFailure(Call<List<MealVO>> call, Throwable t) {
                Log.d("getMealInfo", "getMealInfo Fail");
                Log.d("getMealInfo", "Meal 테이블에 정보가 없어서 나오는 에러일 가능성 높음.");
//                t.printStackTrace();
            }
        });
    }//getMealInfo

    private void getRDI(int mno) {
        retrofitInterface.getInfo(mno).enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                MemberVO info = response.body();
                Float gen = info.getGender() == 1 ? 0.9f : 0.85f;
                rdi = (int)((info.getHeight()-100) * gen * 30);
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                Log.d("getRDI", "getRDI Fail");
                Log.d("getRDI", "Member 테이블에 정보가 없거나");
                Log.d("getRDI", "로그인이 안돼있어 발생할 가능성 높음");
//                t.printStackTrace();
            }
        });
    }//getRDI

    private void getDday(String date, int mno) {
        retrofitInterface.getDday(date, mno).enqueue(new Callback<GoalVO>() {
            @Override
            public void onResponse(Call<GoalVO> call, Response<GoalVO> response) {
                goalDday = response.body();
                recentGoal.setText(goalDday.getGtitle());
                if (ChronoUnit.DAYS.between(LocalDate.parse(date), LocalDate.parse(goalDday.getFinDate())) > 0) {
                    if(ChronoUnit.DAYS.between(LocalDate.parse(date), LocalDate.parse(goalDday.getFinDate())) < 7) {
                        dDay.setTextColor(Color.parseColor("#FF5E00"));
                    }
                    dDay.setText("D-" + (ChronoUnit.DAYS.between(LocalDate.parse(date), LocalDate.parse(goalDday.getFinDate()))));
                } else if (ChronoUnit.DAYS.between(LocalDate.parse(date), LocalDate.parse(goalDday.getFinDate())) == 0) {
                    dDay.setText("Dday");
                    dDay.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void onFailure(Call<GoalVO> call, Throwable t) {
                Log.e("에러", t+"");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 200) {
            if(resultCode != 0) {
                mno = resultCode;
            }
            getRDI(mno);
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            getMealInfo(date, mno);
            dailySumKcal(date, mno);
            getDailyWater(date, mno);
            getProfileInfo(mno);
            getDday(date, mno);
        } else {
            Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }//onActivityResult



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//            drawerLayout.openDrawer(drawerView);
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
//            drawerLayout.openDrawer(drawerView);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            hitext.setVisibility(View.VISIBLE);
        }

        @Override
        public void onDrawerStateChanged(int newState) { }
    };//listener

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로가기\' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }

//        finishAffinity();
//        System.runFinalization();
//        System.exit(0);
    }
}//class