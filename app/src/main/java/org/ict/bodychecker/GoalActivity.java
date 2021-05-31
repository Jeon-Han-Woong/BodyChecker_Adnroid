package org.ict.bodychecker;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class GoalActivity extends AppCompatActivity {

    DoingFragment fragmentDoing;
    FinishFragment fragmentFinish;

    Intent intent;
    int mno = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        fragmentDoing = new DoingFragment();
        fragmentFinish = new FinishFragment();

        intent = getIntent();
        mno = intent.getIntExtra("mno", 0);

        Bundle bundle = new Bundle(1);
        bundle.putInt("mno", mno);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_goal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentDoing.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragmentDoing).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);



        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0) {
                    selected = fragmentDoing;
                    selected.setArguments(bundle);
                } else if (position == 1) {
                    selected = fragmentFinish;
                    selected.setArguments(bundle);
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
