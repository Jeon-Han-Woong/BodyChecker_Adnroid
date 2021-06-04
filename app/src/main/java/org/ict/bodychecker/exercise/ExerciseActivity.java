package org.ict.bodychecker.exercise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.ict.bodychecker.R;
import org.ict.bodychecker.ValueObject.ExerciseVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ExerciseActivity extends AppCompatActivity {

    private View dialog;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView3;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    private Spinner spinner_exer;

    String[] exeritems = {"걷기", "달리기", "등산", "수영", "계단 오르기", "요가", "복싱", "스쿼트", "윗몸 일으키기", "훌라후프", "줄넘기", "자전거", "사이클", "스쿼시", "런닝머신", "에어로빅"};

    float[] exerkcal = {0.067f, 0.123f, 0.14f, 0.158f, 0.123f, 0.044f, 0.175f, 0.123f, 0.14f, 0.07f, 0.175f, 0.14f, 0.123f, 0.21f, 0.184f, 0.105f};
    String selectexer;
    float selectitem;

    int mno = 0;
    Intent intent;


    LocalDateTime today = LocalDateTime.now();
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter btnFormat = DateTimeFormatter.ofPattern("MM/dd");

    String date = dbFormat.format(today);

    int temp_min, sel_index, temp_Eno;
    float temp_kg = 87.4f;
    int temp_result;

    View exer_calendar;
    DatePicker exerDatePick;
    EditText edtminute;
    TextView resultkcal, consumeKcal;
    MaterialButton eMoreDaysAgoBtn, eSixDaysAgoBtn, eFiveDaysAgoBtn, eFourDaysAgoBtn, eThreeDaysAgoBtn, eTwoDaysAgoBtn, eOneDaysAgoBtn, eTodayBtn;
    LinearLayout newExerciseBtn;

    List<Integer> sel_spinner;
    List<ExerciseVO> exerList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        sel_spinner = new ArrayList<>();

        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();

        mno = intent.getIntExtra("mno", 0);

        consumeKcal = (TextView) findViewById(R.id.consumeKcal);

        newExerciseBtn = (LinearLayout) findViewById(R.id.newExerciseBtn);

        exer_calendar = (View) View.inflate(ExerciseActivity.this, R.layout.exercise_calendar, null);

        eMoreDaysAgoBtn = (MaterialButton) findViewById(R.id.eMoreDaysAgoBtn);
        eSixDaysAgoBtn = (MaterialButton) findViewById(R.id.eSixDaysAgoBtn);
        eFiveDaysAgoBtn = (MaterialButton) findViewById(R.id.eFiveDaysAgoBtn);
        eFourDaysAgoBtn = (MaterialButton) findViewById(R.id.eFourDaysAgoBtn);
        eThreeDaysAgoBtn = (MaterialButton) findViewById(R.id.eThreeDaysAgoBtn);
        eTwoDaysAgoBtn = (MaterialButton) findViewById(R.id.eTwoDaysAgoBtn);
        eOneDaysAgoBtn = (MaterialButton) findViewById(R.id.eOneDaysAgoBtn);
        eTodayBtn = (MaterialButton) findViewById(R.id.eTodayBtn);

        eTodayBtn.setText(setBtnDate(0));
        eOneDaysAgoBtn.setText(setBtnDate(-1));
        eTwoDaysAgoBtn.setText(setBtnDate(-2));
        eThreeDaysAgoBtn.setText(setBtnDate(-3));
        eFourDaysAgoBtn.setText(setBtnDate(-4));
        eFiveDaysAgoBtn.setText(setBtnDate(-5));
        eSixDaysAgoBtn.setText(setBtnDate(-6));


        retrofitClient = RetrofitClient.getInstance();

        retrofitInterface = RetrofitClient.getRetrofitInterface();

        retrofitInterface.getDailyExer(date, mno).enqueue(new Callback<List<ExerciseVO>>() {
            @Override
            public void onResponse(Call<List<ExerciseVO>> call, Response<List<ExerciseVO>> response) {
                if (response.isSuccessful()) {

                    exerList = response.body();

                    adapter = new RecyclerAdapter(exerList);

                    recyclerView3.setAdapter(adapter);

                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(ExerciseActivity.this, "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseVO>> call, Throwable t) {
                Toast.makeText(ExerciseActivity.this, "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        getDailySum(date);

        eTodayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                today = LocalDateTime.now();
                date = dbFormat.format(today);
                getDBList(date);
                getDailySum(date);
            }
        });//today.onclick

        eOneDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-1));
                getDBList(date);
                getDailySum(date);
            }
        });//oneDayAgo.onclick

        eTwoDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-2));
                getDBList(date);
                getDailySum(date);
            }
        });//oneDayAgo.onclick

        eThreeDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-3));
                getDBList(date);
                getDailySum(date);
            }
        });//oneDayAgo.onclick

        eFourDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-4));
                getDBList(date);
                getDailySum(date);
            }
        });//oneDayAgo.onclick

        eFiveDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-5));
                getDBList(date);
                getDailySum(date);
            }
        });//oneDayAgo.onclick

        eSixDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dbFormat.format(today.plusDays(-6));
                getDBList(date);
                getDailySum(date);
            }
        });//oneDayAgo.onclick

        eMoreDaysAgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(ExerciseActivity.this);
                dlg.setTitle("날짜 선택");
                if (exer_calendar.getParent() != null) {
                    ((ViewGroup) exer_calendar.getParent()).removeView(exer_calendar);
                }
                dlg.setView(exer_calendar);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exerDatePick = (DatePicker) exer_calendar.findViewById(R.id.exerDatePick);

                        date = exerDatePick.getYear() + "-" + String.format("%02d", exerDatePick.getMonth() + 1) + "-" + String.format("%02d", exerDatePick.getDayOfMonth());
                        getDBList(date);
                        getDailySum(date);
                    }
                });//dlg.positive

                dlg.setNegativeButton("취소", null);

                dlg.show();
            }
        });
        

        newExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = (View) View.inflate(ExerciseActivity.this, R.layout.exercise_dialog, null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(ExerciseActivity.this);
                spinner_exer = (Spinner) dialog.findViewById(R.id.exer_spinner);
                edtminute = (EditText) dialog.findViewById(R.id.edtminute);
                resultkcal = (TextView) dialog.findViewById(R.id.resultkcal);


                retrofitInterface.getNewEno().enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        temp_Eno = response.body() + 1;
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_spinner_item, exeritems);

                ExerciseVO newExerData = new ExerciseVO();

                spinner_exer.setAdapter(adapter1);

                spinner_exer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectitem = exerkcal[i];
                        selectexer = exeritems[i];
                        sel_index = i;
                        try {
                            temp_min = Integer.parseInt(edtminute.getText().toString());
                            temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                            resultkcal.setText(temp_result + " kcal");
                        } catch (NumberFormatException e) {
                            temp_min = 0;
                            temp_result = 0;
                            resultkcal.setText(temp_result + " kcal");
                            return;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                edtminute.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        try {

                            temp_min = Integer.parseInt(edtminute.getText().toString());
                            temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                            resultkcal.setText(temp_result + " kcal");
                        } catch (NumberFormatException e) {
                            temp_min = 0;
                            temp_result = 0;
                            resultkcal.setText(temp_result + " kcal");
                            return;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                dlg.setTitle("운동 기록 추가");

                dlg.setView(dialog);

                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                dlg.setNegativeButton("취소", null);

                final AlertDialog dia = dlg.create();

                dia.show();

                dia.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newExerData.setEname(selectexer);
                        try {

                            newExerData.setEtime(Integer.parseInt(edtminute.getText().toString()));
                        } catch (NumberFormatException e) {

                            Toast.makeText(ExerciseActivity.this, "\'분\'은 숫자만 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        newExerData.setEkcal(temp_result);
                        newExerData.setEdate(date);
                        newExerData.setMno(mno);
                        newExerData.setEno(temp_Eno);

                        sel_spinner.add(sel_index);

                        retrofitInterface.registerExer(newExerData).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(ExerciseActivity.this, "운동 정보를 추가했습니다.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(ExerciseActivity.this, "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        adapter.addItem(newExerData);
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getDailySum(date);

                        adapter.notifyDataSetChanged();

                        dia.dismiss();
                    }
                });
            }
        });


    }

    private String setBtnDate(int n) {
        return btnFormat.format(today.plusDays(n));
    }//getDate

    private void getDBList(String date) {

        retrofitInterface.getDailyExer(date, mno).enqueue(new Callback<List<ExerciseVO>>() {
            @Override
            public void onResponse(Call<List<ExerciseVO>> call, Response<List<ExerciseVO>> response) {
                if (response.isSuccessful()) {

                    exerList = response.body();

                    adapter = new RecyclerAdapter(exerList);

                    recyclerView3.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ExerciseActivity.this, "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseVO>> call, Throwable t) {
                Toast.makeText(ExerciseActivity.this, "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDailySum(String date) {

        retrofitInterface.getSumKcal(date, mno).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                consumeKcal.setText(response.body() + " kcal");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                consumeKcal.setText(0 + " kcal");
            }
        });
    }

    private void init() {
        recyclerView3 = findViewById(R.id.recyclerView3);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(linearLayoutManager);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private List<ExerciseVO> listData;

        private RecyclerAdapter(List<ExerciseVO> listData) {
            this.listData = listData;
        }

        ;

        @NonNull
        @Override
        public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
            return new RecyclerAdapter.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ItemViewHolder holder, int position) {
            holder.onBind(listData.get(position));
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        void addItem(ExerciseVO data) {
            listData.add(data);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView exerciseTitle, exerciseKcal, myEno;

            ItemViewHolder(View itemView) {
                super(itemView);

                exerciseTitle = itemView.findViewById(R.id.exerciseTitle);
                exerciseKcal = itemView.findViewById(R.id.exerciseKcal);
                myEno = itemView.findViewById(R.id.myEno);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ExerciseVO sel_data = listData.get(getAdapterPosition());

                        dialog = (View) View.inflate(ExerciseActivity.this, R.layout.exercise_dialog, null);

                        AlertDialog.Builder dlg = new AlertDialog.Builder(ExerciseActivity.this);
                        spinner_exer = (Spinner) dialog.findViewById(R.id.exer_spinner);

                        edtminute = (EditText) dialog.findViewById(R.id.edtminute);
                        resultkcal = (TextView) dialog.findViewById(R.id.resultkcal);

                        edtminute.setText(sel_data.getEtime() + "");

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_spinner_item, exeritems);

                        spinner_exer.setAdapter(adapter1);


                        spinner_exer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectitem = exerkcal[i];
                                selectexer = exeritems[i];
                                sel_index = i;
                                try {

                                    temp_min = Integer.parseInt(edtminute.getText().toString());
                                    temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                                    resultkcal.setText(temp_result + " kcal");
                                } catch (NumberFormatException e) {
                                    temp_min = 0;
                                    temp_result = 0;
                                    resultkcal.setText(temp_result + " kcal");
                                    return;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        edtminute.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                try {
                                    temp_min = Integer.parseInt(edtminute.getText().toString());
                                    temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                                    resultkcal.setText(temp_result + " kcal");
                                } catch (NumberFormatException e) {
                                    temp_min = 0;
                                    temp_result = 0;
                                    resultkcal.setText(temp_result + " kcal");
                                    return;
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        dlg.setTitle("운동 기록 변경");

                        dlg.setView(dialog);

                        dlg.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sel_data.setEname(selectexer);
                                sel_data.setEtime(Integer.parseInt(edtminute.getText().toString()));
                                sel_data.setEkcal(temp_result);
                                listData.set(getAdapterPosition(), sel_data);
                                retrofitInterface.modifyExer(Integer.parseInt(myEno.getText().toString()), sel_data).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Toast.makeText(ExerciseActivity.this, "운동 정보를 변경하였습니다.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(ExerciseActivity.this, "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        Log.d("에러", t + "");
                                    }
                                });
                                try {
                                    TimeUnit.MILLISECONDS.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                getDailySum(date);

                                adapter.notifyDataSetChanged();
                            }
                        });
                        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                retrofitInterface.removeExer(Integer.parseInt(myEno.getText().toString())).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(ExerciseActivity.this, "운동 정보를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(ExerciseActivity.this, "시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                try {
                                    TimeUnit.MILLISECONDS.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                getDailySum(date);

                                listData.remove(getAdapterPosition());

                                adapter.notifyDataSetChanged();
                            }
                        });

                        dlg.show();
                    }
                });
            }
            void onBind(ExerciseVO data) {
                exerciseTitle.setText(data.getEname());
                exerciseKcal.setText(data.getEkcal() + "");
                myEno.setText(data.getEno() + "");
            }
        }
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