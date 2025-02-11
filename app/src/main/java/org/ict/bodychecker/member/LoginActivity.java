package org.ict.bodychecker.member;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.R;

import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    SharedPreferences autoLogin;

    CheckBox auto_login;
    EditText login_id, login_pwd;
    Button btnLogin, btnJoin;

    int userMno = 0;
    String id = null, pwd = null;

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        autoLogin = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        userMno = autoLogin.getInt("MNO", 0);
        if(userMno > 0) {
            setResult(userMno);
            finish();
        }

        auto_login = (CheckBox) findViewById(R.id.auto_login);
        login_id = (EditText) findViewById(R.id.login_id);
        login_pwd = (EditText) findViewById(R.id.login_pwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        retrofitClient = retrofitClient.getInstance();

        retrofitInterface = RetrofitClient.getRetrofitInterface();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = login_id.getText().toString().trim();
                pwd = login_pwd.getText().toString().trim();

                if(id.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(id, pwd);
            }
        });//btnLoginOnClick

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent joinIntent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(joinIntent);
            }
        });
    }//onCreate

    private void login(String id, String pwd) {
        MemberVO loginVO = new MemberVO();
        loginVO.setMid(id);
        loginVO.setPwd(pwd);
        retrofitInterface.login(loginVO).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                try {
                    userMno = response.body();
                } catch (NullPointerException e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                setResult(userMno);

                if(auto_login.isChecked()) {//자동로그인이 체크 되어있을경우
                    SharedPreferences.Editor editor = autoLogin.edit();
                    editor.putInt("MNO", userMno);
                    editor.commit();
                }

                finish();
            }//onResponse

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }//onFailure
        });
    }//login

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로가기\' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }

    }
}//class
