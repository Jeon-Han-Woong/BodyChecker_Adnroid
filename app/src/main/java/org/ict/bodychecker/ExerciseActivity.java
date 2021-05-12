package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private View dialog;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView3;
    private Spinner spinner_exer;

    String [] exeritems = {"걷기", "달리기", "계단 오르기", "스쿼트", "윗몸 일으키기", "훌라후프"};

    float [] exerkcal = {0.067f, 0.123f, 0.123f, 0.123f, 0.14f, 0.07f};
    String selectexer;
    float selectitem;

    Button accountkcal;

    int temp_min, sel_index;
    float temp_kg = 87.4f;
    int temp_result, temp_result_kcal;

    EditText edtminute;
    TextView resultkcal, consumeKcal;
    LinearLayout newExerciseBtn;

    List<Integer> sel_spinner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        sel_spinner = new ArrayList<>();

        init();
        getData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        consumeKcal = (TextView) findViewById(R.id.consumeKcal);

        newExerciseBtn = (LinearLayout) findViewById(R.id.newExerciseBtn);

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

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_spinner_item, exeritems);

                Data3 data = new Data3();


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
                        data.setExerciseTitle(selectexer);
                        data.setExerciseMin(Integer.parseInt(edtminute.getText().toString()));
                        data.setExerciseKcal(temp_result);

                        adapter.addItem(data);

                        temp_result_kcal += temp_result;

                        consumeKcal.setText(temp_result_kcal + " kcal");
                        sel_spinner.add(sel_index);

                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("취소", null);

                dlg.show();
            }
        });
    }



    private void init() {
        recyclerView3 = findViewById(R.id.recyclerView3);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView3.setAdapter(adapter);
    }

    private void getData() {
        List<String> listTitle = new ArrayList<>();

        List<Integer> listContent = new ArrayList<>();

        for (int i = 0; i < listTitle.size(); i++) {

            Data3 data = new Data3();
            data.setExerciseTitle(listTitle.get(i));
            data.setExerciseKcal(listContent.get(i));

            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }



    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private ArrayList<Data3> listData = new ArrayList<Data3>();

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

        void addItem(Data3 data) {
            listData.add(data);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView exerciseTitle, exerciseKcal;

            ItemViewHolder(View itemView) {
                super(itemView);

                exerciseTitle = itemView.findViewById(R.id.exerciseTitle);
                exerciseKcal = itemView.findViewById(R.id.exerciseKcal);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity().getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();
                        Data3 sel_data = listData.get(getAdapterPosition());

                        dialog = (View) View.inflate(ExerciseActivity.this, R.layout.exercise_dialog, null);

                        AlertDialog.Builder dlg = new AlertDialog.Builder(ExerciseActivity.this);
                        spinner_exer = (Spinner) dialog.findViewById(R.id.exer_spinner);

                        edtminute = (EditText) dialog.findViewById(R.id.edtminute);
                        resultkcal = (TextView) dialog.findViewById(R.id.resultkcal);
                        accountkcal = (Button) dialog.findViewById(R.id.accountkcal);

                        edtminute.setText(sel_data.getExerciseMin()+"");

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_spinner_item, exeritems);

                        spinner_exer.setAdapter(adapter1);

                        spinner_exer.setSelection(sel_spinner.get(getAdapterPosition()));

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
                        Data3 old_data = listData.get(getAdapterPosition());
                        int old_kcal = old_data.getExerciseKcal();

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
                                sel_data.setExerciseTitle(selectexer);
                                sel_data.setExerciseMin(Integer.parseInt(edtminute.getText().toString()));
                                sel_data.setExerciseKcal(temp_result);


//                                Toast.makeText(ExerciseActivity.this, temp_result+"", Toast.LENGTH_SHORT).show();
                                temp_result_kcal = temp_result_kcal - old_kcal + temp_result;

                                consumeKcal.setText(temp_result_kcal + " kcal");
                                sel_spinner.set(getAdapterPosition(), sel_index);

                                listData.set(getAdapterPosition(), sel_data);
                                adapter.notifyDataSetChanged();
                            }
                        });

                        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int del_kcal = sel_data.getExerciseKcal();

                                temp_result_kcal = temp_result_kcal - del_kcal;

                                consumeKcal.setText(temp_result_kcal + " kcal");

                                listData.remove(getAdapterPosition());

                                adapter.notifyDataSetChanged();
                            }
                        });

                        dlg.show();
                    }
                });
            }

            void onBind(Data3 data) {
                exerciseTitle.setText(data.getExerciseTitle());
                exerciseKcal.setText(data.getExerciseKcal()+"");
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
