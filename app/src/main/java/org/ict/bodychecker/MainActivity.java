package org.ict.bodychecker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Context context = this;
    View drawerView;
    ListView listView;
    TextView hitext;
    ProgressBar walkProgress, waterProgress;
    LinearLayout goExerciseBtn, goMealBtn;
    TextView drinkWater, nowWater;
    Button waterPlus, waterMinus;
    int temp_water;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goExerciseBtn = (LinearLayout) findViewById(R.id.goExerciseBtn);
        goMealBtn = (LinearLayout) findViewById(R.id.goMealBtn);
        waterPlus = (Button) findViewById(R.id.waterPlus);
        waterMinus = (Button) findViewById(R.id.waterMinus);
        drinkWater = (TextView) findViewById(R.id.drinkWater);
        nowWater = (TextView) findViewById(R.id.nowWater);
        waterProgress = (ProgressBar) findViewById(R.id.waterProgress);

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
        });

    }



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