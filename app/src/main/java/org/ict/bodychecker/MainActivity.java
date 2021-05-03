package org.ict.bodychecker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    LinearLayout goExerciseBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goExerciseBtn = (LinearLayout) findViewById(R.id.goExerciseBtn);

        goExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);

                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    Toast.makeText(getApplicationContext(), "프로필", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.myGoal) {
                    Intent intent = new Intent(getApplicationContext(), GoalActivity.class);

                    startActivity(intent);
                } else if (id == R.id.notice) {
                    Toast.makeText(getApplicationContext(), "공지사항", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.FAQ) {
                    Toast.makeText(getApplicationContext(), "FAQ", Toast.LENGTH_SHORT).show();
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