package org.ict.bodychecker.member;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ict.bodychecker.R;
import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JoinActivity extends AppCompatActivity {

    MemberVO memberVO = new MemberVO();

    LocalDateTime today = LocalDateTime.now();
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //checked : 0 - 회원가입 가능, 1 - 회원가입불가
    int checked = 1, height = 0, weight = 0, bmi = 0;
    String mid = null, name = null, pwd = null, birthday = null;

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    RadioGroup join_gender;
    Button join_idCheck, joinBtn, joinCancelBtn;
    TextView idCheck;
    EditText join_name, join_id, join_pwd, join_pwdChk, join_height, join_weight;
    DatePicker join_birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        join_gender = (RadioGroup) findViewById(R.id.join_gender);
        join_idCheck = (Button) findViewById(R.id.join_idCheck);
        joinBtn = (Button) findViewById(R.id.joinBtn);
        joinCancelBtn = (Button) findViewById(R.id.joinCancelBtn);
        idCheck = (TextView) findViewById(R.id.idCheck);
        join_birthday = (DatePicker) findViewById(R.id.join_birthday);

        join_name = (EditText) findViewById(R.id.join_name);
        join_id = (EditText) findViewById(R.id.join_id);
        join_pwd = (EditText) findViewById(R.id.join_pwd);
        join_pwdChk = (EditText) findViewById(R.id.join_pwdChk);
        join_height = (EditText) findViewById(R.id.join_height);
        join_weight = (EditText) findViewById(R.id.join_weight);

        join_idCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mid = join_id.getText().toString().trim();
                if(mid != null && !mid.equals("")) {
                    retrofitInterface.check(mid).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d("check", response.body());
                            if(response.body().equals("0")) {
                                idCheck.setTextColor(Color.BLUE);
                                idCheck.setText("사용 가능한 아이디입니다.");
                                memberVO.setMid(mid);
                                checked = 0;
                            } else {
                                idCheck.setTextColor(Color.RED);
                                idCheck.setText("이미 존재하는 아이디입니다.");
                            }//else
                        }//onResponse

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            t.printStackTrace();
                        }//onFailure
                    });//check
                } else {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }//else

            }
        });//join_idCheckOnClick

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(join_name.getText().toString().trim() == null || join_name.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(checked != 0 || !memberVO.getMid().equals(join_id.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "아이디 중복체크를 진행해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!join_pwd.getText().toString().equals(join_pwdChk.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    switch(join_gender.getCheckedRadioButtonId()) {
                        case R.id.join_male:
                            memberVO.setGender(1);
                            break;
                        case R.id.join_female:
                            memberVO.setGender(2);
                    }//switch

                    name = join_name.getText().toString().trim();
                    mid = join_id.getText().toString().trim();
                    pwd = join_pwd.getText().toString().trim();
                    height = Math.round(Float.parseFloat(join_height.getText().toString().trim()));
                    weight = Math.round(Float.parseFloat(join_weight.getText().toString().trim()));
                    try {
                        bmi = (int)(weight/((height*0.01)*(height*0.01)));
                        if(bmi < 0 || height == 0 || weight == 0) {
                            Toast.makeText(getApplicationContext(), "키와 체중은 0이하가 될 수 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch(ArithmeticException e) {
                        Toast.makeText(getApplicationContext(), "키와 체중은 0이하가 될 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    birthday = join_birthday.getYear() + "-" + String.format("%02d", join_birthday.getMonth()+1) + "-" + String.format("%02d", join_birthday.getDayOfMonth());

                    if(name!=null && !name.equals("")) memberVO.setMname(name);
                    else return;
                    if(mid!=null && !mid.equals("")) memberVO.setMid(mid);
                    else return;
                    if(pwd!=null && !pwd.equals("")) memberVO.setPwd(pwd);
                    else return;
                    if(height > 0) memberVO.setHeight(height);
                    else return;
                    if(weight > 0) memberVO.setWeight(weight);
                    else return;
                    if(birthday!=null && !birthday.equals("")) memberVO.setBirthday(birthday);
                    else return;
                    if(bmi > 0) memberVO.setBmi(bmi);
                    else return;
                    memberVO.setRegdate(dbFormat.format(today));

                    retrofitInterface.join(memberVO).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                            finish();
                        }//onResponse

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        }//onFailure
                    });//join
                }//else
            }
        });//joinBtnOnClick

        joinCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}//class
