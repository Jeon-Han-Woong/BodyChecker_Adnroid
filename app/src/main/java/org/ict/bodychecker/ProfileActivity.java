package org.ict.bodychecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    Button modifyInfoBtn, join;
    ImageView genderImg;
    TextView username, genderTV, heightTV, weightTV, birthTV, joindateTV;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        modifyInfoBtn = (Button) findViewById(R.id.modifyInfoBtn);
        join = (Button) findViewById(R.id.join);
        username = (TextView) findViewById(R.id.username);

        genderImg = (ImageView) findViewById(R.id.genderImg);
        genderTV = (TextView) findViewById(R.id.genderTV);
        heightTV = (TextView) findViewById(R.id.heightTV);
        weightTV = (TextView) findViewById(R.id.weightTV);
        birthTV = (TextView) findViewById(R.id.birthTV);
        joindateTV = (TextView) findViewById(R.id.joindateTV);

        getInfo(2);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent joinIntent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(joinIntent);
            }
        });

        modifyInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ModifyInfoActivity.class);
                startActivity(intent);
            }
        });
    }//onCreate

    private void getInfo(int mno) {
        retrofitInterface.getInfo(mno).enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                MemberVO info = response.body();
                username.setText(info.getMname());
                if(info.getGender() == 1) {
                    genderImg.setImageResource(R.drawable.porfile_male);
                    genderTV.setText("남자");
                } else if(info.getGender() == 2) {
                    genderImg.setImageResource(R.drawable.porfile_female);
                    genderTV.setText("여자");
                }
                heightTV.setText(String.valueOf(info.getHeight()).concat("cm"));
                weightTV.setText(String.valueOf(info.getWeight()).concat("kg"));
                birthTV.setText(info.getBirthday());
                joindateTV.setText(info.getRegdate());
            }//onResponse

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {

            }//onFailure
        });
    }//getInfo

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
