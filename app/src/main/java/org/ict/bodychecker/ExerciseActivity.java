package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private View dialog;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView3;
    private Spinner spinner_exer;

    String [] exeritems = {"걷기", "달리기", "계단 오르기", "스쿼트", "윗몸 일으키기", "훌라후프",};

    float [] exerkcal = {0.067f, 0.123f, 0.123f, 0.123f, 0.14f, 0.07f};
    String selectexer;
    float selectitem;

    Button accountkcal;

    int temp_min;
    float temp_kg = 87.4f;
    int temp_result, temp_result_kcal;

    EditText edtminute;
    TextView resultkcal, consumeKcal;
    LinearLayout newExerciseBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                accountkcal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_min = Integer.parseInt(edtminute.getText().toString());
                        temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                        resultkcal.setText(temp_result + "kcal");
                    }
                });

                dlg.setTitle("운동 기록 추가");

                dlg.setView(dialog);

                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        data.setExerciseTitle(selectexer);
                        data.setExerciseKcal(temp_result + "kcal");

                        adapter.addItem(data);

                        temp_result_kcal += temp_result;

                        consumeKcal.setText(temp_result_kcal + "kcal");

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

        List<String> listContent = new ArrayList<>();

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
                        dialog = (View) View.inflate(ExerciseActivity.this, R.layout.exercise_dialog, null);

                        AlertDialog.Builder dlg = new AlertDialog.Builder(ExerciseActivity.this);
                        spinner_exer = (Spinner) dialog.findViewById(R.id.exer_spinner);

                        edtminute = (EditText) dialog.findViewById(R.id.edtminute);
                        resultkcal = (TextView) dialog.findViewById(R.id.resultkcal);
                        accountkcal = (Button) dialog.findViewById(R.id.accountkcal);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_spinner_item, exeritems);

                        spinner_exer.setAdapter(adapter);

                        spinner_exer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectitem = exerkcal[i];
                                selectexer = exeritems[i];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

//                        edtminute.setText(exerciseTitle);


                        accountkcal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                temp_min = Integer.parseInt(edtminute.getText().toString());
                                temp_result = Math.round(temp_kg * (float) temp_min * selectitem);
                                resultkcal.setText(temp_result + "kcal");
                            }
                        });

                        dlg.setTitle("운동 기록 변경");

                        dlg.setView(dialog);

                        dlg.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });

                        dlg.show();
                    }
                });
            }

            void onBind(Data3 data) {
                exerciseTitle.setText(data.getExerciseTitle());
                exerciseKcal.setText(data.getExerciseKcal());
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
