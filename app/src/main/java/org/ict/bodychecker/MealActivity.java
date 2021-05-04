package org.ict.bodychecker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.material.button.MaterialButton;

public class MealActivity extends AppCompatActivity {

    CircleProgressBar circlebar;

    MaterialButton moreDaysAgo, sixDaysAgo, fiveDaysAgo, fourDaysAgo, threeDaysAgo, twoDaysAgo, oneDaysAgo, today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);

        circlebar = (CircleProgressBar) findViewById(R.id.circlebar);
        circlebar.setProgress(100);

        moreDaysAgo = (MaterialButton) findViewById(R.id.moreDaysAgo);
        sixDaysAgo = (MaterialButton) findViewById(R.id.sixDaysAgo);
        fiveDaysAgo = (MaterialButton) findViewById(R.id.fiveDaysAgo);
        fourDaysAgo = (MaterialButton) findViewById(R.id.fourDaysAgo);
        threeDaysAgo = (MaterialButton) findViewById(R.id.threeDaysAgo);
        twoDaysAgo = (MaterialButton) findViewById(R.id.twoDaysAgo);
        oneDaysAgo = (MaterialButton) findViewById(R.id.oneDaysAgo);
        today = (MaterialButton) findViewById(R.id.today);


    }
}
