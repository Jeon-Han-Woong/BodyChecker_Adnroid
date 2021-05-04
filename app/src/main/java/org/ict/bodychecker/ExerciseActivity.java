package org.ict.bodychecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        getData();
    }



    private void init() {
        recyclerView3 = findViewById(R.id.recyclerView3);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView3.setAdapter(adapter);
    }

    private void getData() {
        List<String> listTitle = Arrays.asList("1", "2", "3", "1", "2", "3", "1", "2", "3");

        List<String> listContent = Arrays.asList("1a", "2b", "3c", "1a", "2b", "3c", "1a", "2b", "3c");

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

                        dlg.setTitle("목표 상세");

                        dlg.setView(dialog);

                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
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
