package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.ict.bodychecker.ValueObject.ExerciseVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    String [] exeritems = {"걷기", "달리기", "계단 오르기", "스쿼트", "윗몸 일으키기", "훌라후프"};

    float [] exerkcal = {0.067f, 0.123f, 0.123f, 0.123f, 0.14f, 0.07f};
    String selectexer;
    float selectitem;

    Button accountkcal;

    LocalDateTime today = LocalDateTime.now();
    DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter btnFormat = DateTimeFormatter.ofPattern("MM/dd");

    final String TIME = "T00:00:00";
    String date = dbFormat.format(today);

    int temp_min, sel_index, temp_Eno;
    float temp_kg = 87.4f;
    int temp_result, temp_result_kcal = 0;

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
//        today = sdf.format(new Date());
        sel_spinner = new ArrayList<>();

        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        init();
//        getData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        retrofitInterface.getDailyExer(date).enqueue(new Callback<List<ExerciseVO>>() {
            @Override
            public void onResponse(Call<List<ExerciseVO>> call, Response<List<ExerciseVO>> response) {
                if (response.isSuccessful()) {

                    exerList = response.body();

                    Log.d("샘플", exerList+"");

                    adapter = new RecyclerAdapter(exerList);

                    recyclerView3.setAdapter(adapter);

//                    for (int i = 0; i < exerList.size(); i++) {
//                        temp_result_kcal = temp_result_kcal + exerList.get(i).getEkcal();
//                    }
//
//                    consumeKcal.setText(temp_result_kcal+" kcal");

                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(ExerciseActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseVO>> call, Throwable t) {
                Log.d("에러", t + " : " + call);


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
                if(exer_calendar.getParent() != null) {
                    ((ViewGroup) exer_calendar.getParent()).removeView(exer_calendar);
                }
                dlg.setView(exer_calendar);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exerDatePick = (DatePicker) exer_calendar.findViewById(R.id.exerDatePick);

                        date = exerDatePick.getYear()+"-"+String.format("%02d", exerDatePick.getMonth()+1)+"-"+String.format("%02d", exerDatePick.getDayOfMonth());
                        getDBList(date);
                        getDailySum(date);
                    }
                });//dlg.positive

                dlg.setNegativeButton("취소", null);

                dlg.show();
            }
        });




//        recyclerView3 = findViewById(R.id.recyclerView3);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView3.setLayoutManager(linearLayoutManager);
//
//        adapter = new RecyclerAdapter();
//        recyclerView3.setAdapter(adapter);

        newExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        Toast.makeText(getActivity().getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();
                dialog = (View) View.inflate(ExerciseActivity.this, R.layout.exercise_dialog, null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(ExerciseActivity.this);
                spinner_exer = (Spinner) dialog.findViewById(R.id.exer_spinner);
                edtminute = (EditText) dialog.findViewById(R.id.edtminute);
                resultkcal = (TextView) dialog.findViewById(R.id.resultkcal);
                accountkcal = (Button) dialog.findViewById(R.id.accountkcal);

                retrofitInterface.getNewEno().enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        temp_Eno = response.body() + 1;
                        Toast.makeText(ExerciseActivity.this, temp_Eno+"", Toast.LENGTH_SHORT).show();
                        Log.d("ENO = ", temp_Eno + "");
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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                accountkcal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            temp_min = Integer.parseInt(edtminute.getText().toString());
                            temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                            resultkcal.setText(temp_result + " kcal");
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dlg.setTitle("운동 기록 추가");

                dlg.setView(dialog);

                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newExerData.setEname(selectexer);
                        newExerData.setEtime(Integer.parseInt(edtminute.getText().toString()));
                        newExerData.setEkcal(temp_result);
                        newExerData.setEdate(date);
                        newExerData.setMno(1);
                        newExerData.setEno(temp_Eno);

                        sel_spinner.add(sel_index);

                        retrofitInterface.registerExer(newExerData).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(ExerciseActivity.this, "새 운동 입력", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("오류", t+"");
                            }
                        });
                        adapter.addItem(newExerData);
                        try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
                        getDailySum(date);

                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("취소", null);

                dlg.show();
            }
        });


    }

    private String setBtnDate(int n) {
        return btnFormat.format(today.plusDays(n));
    }//getDate

    private void getDBList(String date) {

        retrofitInterface.getDailyExer(date).enqueue(new Callback<List<ExerciseVO>>() {
            @Override
            public void onResponse(Call<List<ExerciseVO>> call, Response<List<ExerciseVO>> response) {
                if (response.isSuccessful()) {

                    exerList = response.body();

                    Log.d("샘플", exerList+"");

                    adapter = new RecyclerAdapter(exerList);

                    recyclerView3.setAdapter(adapter);


                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(ExerciseActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseVO>> call, Throwable t) {
                Log.d("에러", t + " : " + call);
            }
        });
    }

    private void getDailySum(String date) {
        consumeKcal.setText(0+ " kcal");
        retrofitInterface.getSumKcal(date).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                consumeKcal.setText(response.body() + " kcal");
                Toast.makeText(ExerciseActivity.this, response.body() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }



    private void init() {
        recyclerView3 = findViewById(R.id.recyclerView3);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(linearLayoutManager);
    }

//    private void getData() {
//        List<String> listTitle = new ArrayList<>();
//
//        List<Integer> listContent = new ArrayList<>();
//
//        for (int i = 0; i < listTitle.size(); i++) {
//
//            ExerciseVO data = new ExerciseVO();
//            data.setEname(listTitle.get(i));
//            data.setEkcal(listContent.get(i));
//
//            adapter.addItem(data);
//        }
//
//        adapter.notifyDataSetChanged();
//    }



    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private List<ExerciseVO> listData;

        private RecyclerAdapter(List<ExerciseVO> listData) {
            this.listData = listData;
        };

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
//                        Toast.makeText(getActivity().getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();
                        ExerciseVO sel_data = listData.get(getAdapterPosition());

                        Toast.makeText(ExerciseActivity.this, sel_data.getEno()+"", Toast.LENGTH_SHORT).show();

                        dialog = (View) View.inflate(ExerciseActivity.this, R.layout.exercise_dialog, null);

                        AlertDialog.Builder dlg = new AlertDialog.Builder(ExerciseActivity.this);
                        spinner_exer = (Spinner) dialog.findViewById(R.id.exer_spinner);

                        edtminute = (EditText) dialog.findViewById(R.id.edtminute);
                        resultkcal = (TextView) dialog.findViewById(R.id.resultkcal);
                        accountkcal = (Button) dialog.findViewById(R.id.accountkcal);

                        edtminute.setText(sel_data.getEtime()+"");

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_spinner_item, exeritems);

                        spinner_exer.setAdapter(adapter1);


                        spinner_exer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectitem = exerkcal[i];
                                selectexer = exeritems[i];
                                sel_index = i;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                        // 운동 내역을 변경했을 경우 현재 소모 칼로리를 함께 변경. start
                        ExerciseVO old_data = listData.get(getAdapterPosition());
                        int old_kcal = old_data.getEkcal();

//                        Toast.makeText(getApplicationContext(), int_kcal+ "", Toast.LENGTH_SHORT).show();
                        // end


                        accountkcal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                temp_min = Integer.parseInt(edtminute.getText().toString());
                                temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                                resultkcal.setText(temp_result + " kcal");
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

//                                Toast.makeText(ExerciseActivity.this, temp_result+"", Toast.LENGTH_SHORT).show();
                                listData.set(getAdapterPosition(), sel_data);

                                retrofitInterface.modifyExer(Integer.parseInt(myEno.getText().toString()), sel_data).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Toast.makeText(ExerciseActivity.this, "운동 변경", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(ExerciseActivity.this, "실패", Toast.LENGTH_SHORT).show();
                                        Log.d("에러", t+"");
                                    }
                                });
                                try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
                                getDailySum(date);

                                adapter.notifyDataSetChanged();
                            }
                        });

                        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int del_kcal = sel_data.getEkcal();

                                retrofitInterface.removeExer(Integer.parseInt(myEno.getText().toString())).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(ExerciseActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(ExerciseActivity.this, "실패", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
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
                exerciseKcal.setText(data.getEkcal()+"");
                myEno.setText(data.getEno()+"");
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

//    public class MyDBHelper extends SQLiteOpenHelper {
//        public MyDBHelper(Context context) {
//            super(context, "bodychecker", null, 1);
//        }//생성자
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("CREATE TABLE exercise()");
//        }//onCreate
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//
//        }
//    }
}
