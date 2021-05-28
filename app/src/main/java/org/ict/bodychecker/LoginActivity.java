package org.ict.bodychecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    CheckBox auto_login;
    EditText login_id, login_pwd;
    Button btnLogin, btnJoin;

    int userMno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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
                retrofitInterface.login(login_id.getText().toString(), login_pwd.getText().toString()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        userMno = response.body();
                        Toast.makeText(LoginActivity.this, userMno+"", Toast.LENGTH_SHORT).show();

                        setResult(userMno);

                        finish();
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent joinIntent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(joinIntent);
            }
        });
    }
}
