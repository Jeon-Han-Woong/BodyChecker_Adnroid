package org.ict.bodychecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MealActivity extends AppCompatActivity {

    CircleProgressBar circlebar;
    LinearLayout bfll, lcll, dnll, dsll;
    TextView breakfast, lunch, dinner, disert;
    MaterialButton moreDaysAgo, sixDaysAgo, fiveDaysAgo, fourDaysAgo, threeDaysAgo, twoDaysAgo, oneDaysAgo, today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);

        circlebar = (CircleProgressBar) findViewById(R.id.circlebar);
        circlebar.setProgress(100);

        breakfast = (TextView) findViewById(R.id.breakfast);
        lunch = (TextView) findViewById(R.id.lunch);
        dinner = (TextView) findViewById(R.id.dinner);
        disert = (TextView) findViewById(R.id.disert);

        bfll = (LinearLayout) findViewById(R.id.bfll);
        lcll = (LinearLayout) findViewById(R.id.lcll);
        dnll = (LinearLayout) findViewById(R.id.dnll);
        dsll = (LinearLayout) findViewById(R.id.dsll);

        moreDaysAgo = (MaterialButton) findViewById(R.id.moreDaysAgo);
        sixDaysAgo = (MaterialButton) findViewById(R.id.sixDaysAgo);
        fiveDaysAgo = (MaterialButton) findViewById(R.id.fiveDaysAgo);
        fourDaysAgo = (MaterialButton) findViewById(R.id.fourDaysAgo);
        threeDaysAgo = (MaterialButton) findViewById(R.id.threeDaysAgo);
        twoDaysAgo = (MaterialButton) findViewById(R.id.twoDaysAgo);
        oneDaysAgo = (MaterialButton) findViewById(R.id.oneDaysAgo);
        today = (MaterialButton) findViewById(R.id.today);

        bfll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "bf");
                startActivityForResult(intent, 200);
            }
        });//bfllOnClick

        lcll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "lc");
                startActivityForResult(intent, 200);
            }
        });//lcllOnClick

        dnll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "dn");
                startActivityForResult(intent, 200);
            }
        });//dnllOnClick

        dsll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealSelectActivity.class);
                intent.putExtra("meal", "ds");
                startActivityForResult(intent, 200);
            }
        });//dsllOnClick
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==200) {
            ArrayList<String> list = new ArrayList<>();
            if(data!=null) {
                list = data.getStringArrayListExtra("selected");
            } else {
                list.add("");
            }

            String food = "";

            for(int i=0; i<list.size(); i++) {
                if(!food.equals("")) { food += ", "; }
                food += list.get(i);
            }

            switch(resultCode) {
                case -1: break;//취소 버튼
                case 0://아침
                    breakfast.setText(food);
                    break;
                case 1://점심
                    lunch.setText(food);
                    break;
                case 2://저녁
                    dinner.setText(food);
                    break;
                case 3://간식
                    disert.setText(food);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                    break;
            }//switch
        } else {
            Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
        }//else

        super.onActivityResult(requestCode, resultCode, data);
    }
}
