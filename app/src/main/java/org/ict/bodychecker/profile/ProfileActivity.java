package org.ict.bodychecker.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ict.bodychecker.R;
import org.ict.bodychecker.ValueObject.MemberVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    Button modifyInfoBtn, removeBtn;
    ImageView genderImg;
    TextView username, genderTV, heightTV, weightTV, birthTV, joindateTV, goalTotalTV, goalSuccessTV;
    View remove_confirm;
    EditText removeEdt;

    Intent intent;
    int mno = 0, total = 0, success = 0;
    Boolean confirm = false;

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
        removeBtn = (Button) findViewById(R.id.removeBtn);
        username = (TextView) findViewById(R.id.username);

        genderImg = (ImageView) findViewById(R.id.genderImg);
        genderTV = (TextView) findViewById(R.id.genderTV);
        heightTV = (TextView) findViewById(R.id.heightTV);
        weightTV = (TextView) findViewById(R.id.weightTV);
        birthTV = (TextView) findViewById(R.id.birthTV);
        joindateTV = (TextView) findViewById(R.id.joindateTV);
        goalTotalTV = (TextView) findViewById(R.id.goalTotalTV);
        goalSuccessTV = (TextView) findViewById(R.id.goalSuccessTV);

        remove_confirm = (View) View.inflate(ProfileActivity.this, R.layout.remove_confirm, null);
        removeEdt = (EditText) remove_confirm.findViewById(R.id.removeEdt);

        intent = getIntent();
        mno = intent.getIntExtra("mno", 0);
        getInfo(mno);

        modifyInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ModifyInfoActivity.class);
                intent.putExtra("mno", mno);
                startActivityForResult(intent, 200);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(ProfileActivity.this);
                dlg.setTitle("회원탈퇴");
                dlg.setView(remove_confirm);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });//setPositiveButton
                dlg.setNegativeButton("취소", null);//setNegativeButton

                final AlertDialog dia = dlg.create();

                dia.show();

                dia.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pwd = removeEdt.getText().toString().trim();
                        if(pwd == null || pwd.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            confirm(mno, pwd);
                        }//else
                    }
                });

            }
        });//removeBtnOnClick
    }//onCreate

    private void getInfo(int mno) {
        retrofitInterface.getInfo(mno).enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                MemberVO info = response.body();
                username.setText(info.getMname());
                if(info.getGender() == 1) {
                    genderImg.setImageResource(R.drawable.porfile_male);
                    genderTV.setText("남성");
                } else if(info.getGender() == 2) {
                    genderImg.setImageResource(R.drawable.porfile_female);
                    genderTV.setText("여성");
                }
                heightTV.setText(String.valueOf(info.getHeight()).concat("cm"));
                weightTV.setText(String.valueOf(info.getWeight()).concat("kg"));
                birthTV.setText(info.getBirthday());
                joindateTV.setText(info.getRegdate());
            }//onResponse

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                t.printStackTrace();
            }//onFailure
        });//getInfo

        retrofitInterface.getTotal(mno).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                total = response.body();
                goalTotalTV.setText(total + "개의 목표 중");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });

        retrofitInterface.getsuccessFinish(mno).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                success = response.body();
                goalSuccessTV.setText(success + "개 성공");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });//getsuccessFinish
    }//getInfo

    private void remove(int mno) {
        retrofitInterface.remove(mno).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().equals("SUCCESS")) {
                    SharedPreferences autoLogin = getSharedPreferences("auto", MODE_PRIVATE);
                    SharedPreferences.Editor editor = autoLogin.edit();
                    editor.putInt("MNO", 0);
                    editor.commit();
                    setResult(-1);
                    Toast.makeText(getApplicationContext(), "그동안 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }//else
            }//onResponse

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }//onFailure
        });
    }//remove

    private void confirm(int mno, String pwd) {
        retrofitInterface.confirm(mno, pwd).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if(response.body() != 0) {
                    remove(mno);
                }//else if
            }//onResponse

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }//onFailure
        });
    }//confirm

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 200) {
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            getInfo(mno);
        } else {
            Toast.makeText(getApplicationContext(), "시스템에러가 발생했습니다. 잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }//onActivityResult

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
