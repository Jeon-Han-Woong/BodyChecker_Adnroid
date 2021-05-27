package org.ict.bodychecker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import org.ict.bodychecker.ValueObject.MealVO;
import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    LocalDateTime today = LocalDateTime.now();
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String date = dbFormat.format(today);

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    DrawerLayout drawerLayout;
    Context context = this;
    View drawerView;
    ListView listView;
    TextView hitext;
    ProgressBar walkProgress, waterProgress;
    LinearLayout goExerciseBtn, goMealBtn;
    TextView drinkWater, nowWater, myWeight, myBMI;
    Button waterPlus, waterMinus;
    int temp_water = 0, rdi = 0;

    TextView profileName, profileAge, profileHeight, profileWeight, profileBMI, profileBMIName;
    TextView main_dayKcal, main_maxKcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = getLayoutInflater();
        View navi_header = inflater.inflate(R.layout.navi_header, null);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        goExerciseBtn = (LinearLayout) findViewById(R.id.goExerciseBtn);
        goMealBtn = (LinearLayout) findViewById(R.id.goMealBtn);
        waterPlus = (Button) findViewById(R.id.waterPlus);
        waterMinus = (Button) findViewById(R.id.waterMinus);
        drinkWater = (TextView) findViewById(R.id.drinkWater);
        nowWater = (TextView) findViewById(R.id.nowWater);
        myWeight = (TextView) findViewById(R.id.myWeight);
        myBMI = (TextView) findViewById(R.id.myBMI);
        waterProgress = (ProgressBar) findViewById(R.id.waterProgress);

        profileName = (TextView) navi_header.findViewById(R.id.profileName);
        profileAge = (TextView) navi_header.findViewById(R.id.profileAge);
        profileHeight = (TextView) navi_header.findViewById(R.id.profileHeight);
        profileWeight = (TextView) navi_header.findViewById(R.id.profileWeight);
        profileBMI = (TextView) navi_header.findViewById(R.id.profileBMI);
        profileBMIName = (TextView) navi_header.findViewById(R.id.profileBMIName);

        nowWater.setTextColor(Color.RED);
        temp_water = 0;

        goExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);

                startActivity(intent);
            }
        });

        goMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealActivity.class);
                startActivity(intent);
            }
        });

        waterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp_water += 1;
                waterProgress.setProgress(temp_water * 10);
                if (temp_water < 5) {
                    nowWater.setTextColor(Color.RED);
                } else if (temp_water >= 5 && temp_water < 10) {
                    nowWater.setTextColor(Color.parseColor("#FF5E00"));
                } else if (temp_water >= 10) {
                    nowWater.setTextColor(Color.parseColor("#189186"));
                }

                nowWater.setText((temp_water * 200) + " ml");
                drinkWater.setText(temp_water + "");
            }
        });

        waterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp_water > 0) {
                    temp_water -= 1;
                    waterProgress.setProgress(temp_water * 10);
                    if (temp_water < 5) {
                        nowWater.setTextColor(Color.RED);
                    } else if (temp_water >= 5 && temp_water < 10) {
                        nowWater.setTextColor(Color.parseColor("#FF5E00"));
                    } else if (temp_water >= 10) {
                        nowWater.setTextColor(Color.parseColor("#189186"));
                    }
                    nowWater.setText((temp_water * 200) + " ml");
                } else {
                    Toast.makeText(getApplicationContext(), "물 섭취량은 0보다 낮을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

                drinkWater.setText(temp_water + "");
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
                    startActivity(intent);
                } else if (id == R.id.myGoal) {
                    Intent intent = new Intent(getApplicationContext(), GoalActivity.class);

                    startActivity(intent);
                } else if (id == R.id.notice) {
                    Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.FAQ) {
                    Intent intent = new Intent(getApplicationContext(), FAQActivity.class);
                    startActivity(intent);
                }

                return false;
            }
        });//setNavigationItemSelectedListener
        navigationView.addHeaderView(navi_header);

        getRDI(2);
        try { TimeUnit.MILLISECONDS.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
        getMealInfo();
        getProfileInfo(2);

    }//onCreate

    private void getProfileInfo(int mno) {
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
                profileBMI.setText(String.valueOf(bmi));
                if(bmi < 18.5) {
                    myBMI.setText("확찌자");
                    profileBMIName.setText("확찌자");
                    profileBMIName.setTextColor(Color.rgb(51, 102, 153));
                } else if(18.5 < bmi && bmi < 23) {
                    myBMI.setText("안찐자");
                    profileBMIName.setText("안찐자");
                    profileBMIName.setTextColor(Color.rgb(121, 210, 121));
                } else if(23 <= bmi && bmi < 25) {
                    myBMI.setText("좀찐자");
                    profileBMIName.setText("좀찐자");
                    profileBMIName.setTextColor(Color.rgb(233, 105, 0));
                } else if(25 <= bmi && bmi < 28) {
                    myBMI.setText("확찐자");
                    profileBMIName.setText("확찐자");
                    profileBMIName.setTextColor(Color.rgb(192, 39, 34));
                } else if(28 <= bmi) {
                    myBMI.setText("확!찐자");
                    profileBMIName.setText("확!찐자");
                    profileBMIName.setTextColor(Color.rgb(209, 0, 0));
                }//else if

            }//onResponse

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                t.printStackTrace();
            }//onFailure
        });
    }//getProfileInfo

    private void getMealInfo() {
        retrofitInterface.getDailyMeal(date).enqueue(new Callback<List<MealVO>>() {
            @Override
            public void onResponse(Call<List<MealVO>> call, Response<List<MealVO>> response) {
                main_dayKcal = (TextView) findViewById(R.id.main_dayKcal);
                main_maxKcal = (TextView) findViewById(R.id.main_maxKcal);

                int kcal = response.body().stream().mapToInt(MealVO::getFkcal).sum();
                main_dayKcal.setText(String.valueOf(kcal));
                main_maxKcal.setText("/".concat(String.valueOf(rdi)).concat("kcal"));
            }

            @Override
            public void onFailure(Call<List<MealVO>> call, Throwable t) {
                t.printStackTrace();
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
                t.printStackTrace();
            }
        });
    }//getRDI

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

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
        public void onDrawerStateChanged(int newState) {

        }
    };
}