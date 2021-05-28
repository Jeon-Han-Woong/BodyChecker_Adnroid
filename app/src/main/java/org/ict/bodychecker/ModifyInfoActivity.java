package org.ict.bodychecker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyInfoActivity extends AppCompatActivity {

    MemberVO info = new MemberVO();

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    RadioGroup genderRG;
    RadioButton modi_male, modi_female;
    EditText edtName, edtHeight, edtWeight, modi_pwd, modi_pwdChk;
    DatePicker birthDP;
    Button modifyBtn, modCancelBtn;

    int mno = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        genderRG = (RadioGroup) findViewById(R.id.genderRG);
        modi_male = (RadioButton) findViewById(R.id.modi_male);
        modi_female = (RadioButton) findViewById(R.id.modi_female);
        edtName = (EditText) findViewById(R.id.edtName);
        edtHeight = (EditText) findViewById(R.id.edtHeight);
        edtWeight = (EditText) findViewById(R.id.edtWeight);
        modi_pwd = (EditText) findViewById(R.id.modi_pwd);
        modi_pwdChk = (EditText) findViewById(R.id.modi_pwdChk);
        birthDP = (DatePicker) findViewById(R.id.birthDP);
        modifyBtn = (Button) findViewById(R.id.modifyBtn);
        modCancelBtn = (Button) findViewById(R.id.modCancelBtn);

        Intent getIntent = getIntent();
        mno = getIntent.getIntExtra("mno", 0);

        retrofitInterface.getInfo(mno).enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                info = response.body();
                if(info.getGender() == 1) modi_male.setChecked(true);
                else if(info.getGender() == 2) modi_female.setChecked(true);
                edtName.setText(info.getMname());
                edtHeight.setText(String.valueOf(info.getHeight()));
                edtWeight.setText(String.valueOf(info.getWeight()));
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                t.printStackTrace();
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String pwd = modi_pwd.getText().toString().trim();
                String pwdChk = modi_pwdChk.getText().toString().trim();
                if(pwd.isEmpty() || pwd == null || pwdChk.isEmpty() || pwdChk == null) {
                    Toast.makeText(getApplicationContext(), "비밀번호 또는 비밀번호확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!pwd.equals(pwdChk)) {
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                }
                String birthday = birthDP.getYear() + "-" + String.format("%02d", birthDP.getMonth()+1) + "-" + String.format("%02d", birthDP.getDayOfMonth());
                int height = Math.round(Float.parseFloat(edtHeight.getText().toString().trim()));
                int weight = Math.round(Float.parseFloat(edtWeight.getText().toString().trim()));
                int bmi = 0;
                try {
                    bmi = (int)(weight/((height*0.01)*(height*0.01)));
                } catch(ArithmeticException e) {
                    Toast.makeText(getApplicationContext(), "키는 0이하가 될 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(genderRG.getCheckedRadioButtonId() == R.id.modi_male) {
                    info.setGender(1);
                } else if(genderRG.getCheckedRadioButtonId() == R.id.modi_female) {
                    info.setGender(2);
                }
                if(name!=null && !name.equals("")) info.setMname(name);
                else {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(height > 0) info.setHeight(height);
                else {
                    return;
                }
                if(weight > 0) info.setWeight(weight);
                else {
                    return;
                }
                if(birthday!=null && !birthday.equals("")) info.setBirthday(birthday);
                else {
                    return;
                }
                if(bmi > 0) info.setBmi(bmi);
                else {
                    return;
                }
                info.setPwd(pwd);
                info.setMno(mno);

                retrofitInterface.modifyInfo(info).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getApplicationContext(), "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                finish();
            }
        });

        modCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
