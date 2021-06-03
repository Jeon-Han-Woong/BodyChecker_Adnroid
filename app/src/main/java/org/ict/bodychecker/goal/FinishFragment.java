package org.ict.bodychecker.goal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ict.bodychecker.R;
import org.ict.bodychecker.ValueObject.GoalVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishFragment extends Fragment {

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView2;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    private Activity GoalActivity;
    View dialog;
    TextView finTitle;
    TextView finContent, goalTerm, goalState;
    RadioGroup radioSelect;
    RadioButton btnSuccess, btnFail;
    Button selectComplete;

    int mno = 0;
    Bundle bundle;
    String today;

    List<GoalVO> finishList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_finish, container, false);

        recyclerView2 = (RecyclerView) rootview.findViewById(R.id.recyclerView2);
        init();
        today = String.valueOf(LocalDate.now());

        bundle = getArguments();



        if (bundle != null) {
            mno = bundle.getInt("mno");
        }

        Toast.makeText(GoalActivity, mno+"", Toast.LENGTH_SHORT).show();


        retrofitClient = retrofitClient.getInstance();

        retrofitInterface = RetrofitClient.getRetrofitInterface();

        retrofitInterface.getFinish(today, mno).enqueue(new Callback<List<GoalVO>>() {
            @Override
            public void onResponse(Call<List<GoalVO>> call, Response<List<GoalVO>> response) {
                finishList = response.body();

                adapter = new RecyclerAdapter(finishList);

                recyclerView2.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<GoalVO>> call, Throwable t) {

            }
        });


//        getData();

        return rootview;
    }

    private void init() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView2.setLayoutManager(linearLayoutManager);
    }




//    private void getData() {
//        List<String> listTitle = Arrays.asList("1", "2", "3", "1", "2", "3", "1", "2", "3");
//
//        List<String> listContent = Arrays.asList("1a", "2b", "3c", "1a", "2b", "3c", "1a", "2b", "3c");
//
//        for (int i = 0; i < listTitle.size(); i++) {
//
//            Data2 data = new Data2();
//            data.setFinishTitle(listTitle.get(i));
//
//            adapter.addItem(data);
//        }
//
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            GoalActivity = (Activity) context;
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private List<GoalVO> listData;

        private RecyclerAdapter(List<GoalVO> listData) {
            this.listData = listData;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finish_item, parent, false);

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

        void addItem(GoalVO data) {
            listData.add(data);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView finishTitle, finishDate;
            private LinearLayout finishLayout;

            ItemViewHolder(View itemView) {
                super(itemView);

                finishTitle = itemView.findViewById(R.id.finishTitle);
                finishDate = itemView.findViewById(R.id.finishDate);
                finishLayout = itemView.findViewById(R.id.finishLayout);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoalVO sel_data = listData.get(getAdapterPosition());
                        Toast.makeText(getContext().getApplicationContext(), sel_data.getGno()+"", Toast.LENGTH_SHORT).show();
                        dialog = (View) View.inflate(GoalActivity, R.layout.goal_finish_dialog, null);

                        finTitle = (TextView) dialog.findViewById(R.id.edtFinishTitle);
                        finContent = (TextView) dialog.findViewById(R.id.finishContent);
                        goalTerm = (TextView) dialog.findViewById(R.id.goalTerm);
                        radioSelect = (RadioGroup) dialog.findViewById(R.id.radioSelect);
                        btnSuccess = (RadioButton) dialog.findViewById(R.id.btnSuccess);
                        btnFail = (RadioButton) dialog.findViewById(R.id.btnFail);
                        selectComplete = (Button) dialog.findViewById(R.id.selectComplete);
                        goalState = (TextView) dialog.findViewById(R.id.goalState);


                        finTitle.setText(finishTitle.getText().toString());
                        finContent.setText(sel_data.getGcontent());
                        goalTerm.setText(sel_data.getSetDate() + " ~ " + sel_data.getFinDate());

                        if(sel_data.getGsts() == 0) {
                            radioSelect.setVisibility(View.VISIBLE);
                            btnSuccess.setVisibility(View.VISIBLE);
                            btnFail.setVisibility(View.VISIBLE);
                            selectComplete.setVisibility(View.VISIBLE);
                        } else if (sel_data.getGsts() == 1){
                            radioSelect.setVisibility(View.GONE);
                            btnSuccess.setVisibility(View.GONE);
                            btnFail.setVisibility(View.GONE);
                            selectComplete.setVisibility(View.GONE);
                            goalState.setVisibility(View.VISIBLE);
                            goalState.setText("도전 성공!");
                            goalState.setTextColor(Color.parseColor("#4F66E4"));
                        } else if (sel_data.getGsts() == 2) {
                            radioSelect.setVisibility(View.GONE);
                            btnSuccess.setVisibility(View.GONE);
                            btnFail.setVisibility(View.GONE);
                            selectComplete.setVisibility(View.GONE);
                            goalState.setText("도전 실패!");
                            goalState.setTextColor(Color.parseColor("#D83E72"));
                            goalState.setVisibility(View.VISIBLE);
                        }

                        selectComplete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (radioSelect.getCheckedRadioButtonId() == (R.id.btnSuccess)) {
                                    sel_data.setGsts(1);
                                    retrofitInterface.selectSuccess(sel_data.getGno(), sel_data).enqueue(new Callback<GoalVO>() {
                                        @Override
                                        public void onResponse(Call<GoalVO> call, Response<GoalVO> response) {
                                        }
                                        @Override
                                        public void onFailure(Call<GoalVO> call, Throwable t) {
                                        }
                                    });

                                    radioSelect.setVisibility(View.GONE);
                                    btnSuccess.setVisibility(View.GONE);
                                    btnFail.setVisibility(View.GONE);
                                    selectComplete.setVisibility(View.GONE);
                                    goalState.setVisibility(View.VISIBLE);
                                    goalState.setText("도전 성공!");
                                    goalState.setTextColor(Color.parseColor("#4F66E4"));

                                    listData.set(getAdapterPosition(), sel_data);

                                    adapter.notifyDataSetChanged();
                                } else if (radioSelect.getCheckedRadioButtonId() == (R.id.btnFail)) {
                                    sel_data.setGsts(2);
                                    retrofitInterface.selectSuccess(sel_data.getGno(), sel_data).enqueue(new Callback<GoalVO>() {
                                        @Override
                                        public void onResponse(Call<GoalVO> call, Response<GoalVO> response) {
                                            Log.d("성공", response.body()+"");
                                        }
                                        @Override
                                        public void onFailure(Call<GoalVO> call, Throwable t) {
                                            Log.d("에러", t+"");
                                        }
                                    });

                                    radioSelect.setVisibility(View.GONE);
                                    btnSuccess.setVisibility(View.GONE);
                                    btnFail.setVisibility(View.GONE);
                                    selectComplete.setVisibility(View.GONE);
                                    goalState.setText("도전 실패!");
                                    goalState.setTextColor(Color.parseColor("#D83E72"));
                                    goalState.setVisibility(View.VISIBLE);


                                    Toast.makeText(getContext().getApplicationContext(), sel_data.getGsts()+ "", Toast.LENGTH_SHORT).show();
                                    listData.set(getAdapterPosition(), sel_data);

                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });



//                        edtContent.setText();

                        AlertDialog.Builder dlg = new AlertDialog.Builder(GoalActivity);

                        dlg.setTitle("목표 상세");

                        dlg.setView(dialog);

                        dlg.setPositiveButton("확인", null);

                        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "목표 \"" + finTitle.getText().toString() + "\" 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                                retrofitInterface.removeGoal(sel_data.getGno()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });


                                listData.remove(getAdapterPosition());

                                adapter.notifyDataSetChanged();
                            }

                        });



                        dlg.show();
                    }
                });
            }

            void onBind(GoalVO data) {
                finishTitle.setText(data.getGtitle());
                finishDate.setText("종료 : " + data.getFinDate());

                if (data.getGsts() == 0) {
                    finishLayout.setBackgroundResource(R.drawable.border_choice);

                } else if (data.getGsts() == 1) {
                    finishLayout.setBackgroundResource(R.drawable.border_success);

                } else if (data.getGsts() == 2) {
                    finishLayout.setBackgroundResource(R.drawable.border_fail);

                }
            }
        }
    }
}
