package org.ict.bodychecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ict.bodychecker.ValueObject.GoalVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoingFragment extends Fragment {

    private Activity GoalActivity;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;

    View dialog;

    int mno = 0;
    Bundle bundle;

    EditText edtTitle, edtContent;
    DatePicker dPicker;
    LinearLayout newGoalBtn;

    String today, temp_date, temp_date2;

    private int tYear, tMonth, tDay;
    private int dYear = 1, dMonth = 1, dDay = 1;

    private int temp_gno;

    private long d, t, r;
    boolean tem, exitOk = false;
    private int resultNumber = 0;

    List<GoalVO> doingList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_doing, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        init();
        today = String.valueOf(LocalDate.now());

        bundle = getArguments();

        if (bundle != null) {
            mno = bundle.getInt("mno");
        }

        Toast.makeText(GoalActivity, mno+"", Toast.LENGTH_SHORT).show();
//        getData();

        retrofitClient = retrofitClient.getInstance();

        retrofitInterface = RetrofitClient.getRetrofitInterface();

        retrofitInterface.getDoing(today, mno).enqueue(new Callback<List<GoalVO>>() {
            @Override
            public void onResponse(Call<List<GoalVO>> call, Response<List<GoalVO>> response) {
                doingList = response.body();

                Log.d("샘플", doingList+"");

                adapter = new RecyclerAdapter(doingList);

                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GoalVO>> call, Throwable t) {

            }
        });


        newGoalBtn = (LinearLayout) rootview.findViewById(R.id.newGoalBtn);

        Calendar calendar = Calendar.getInstance();
        tYear = calendar.get(Calendar.YEAR);
        tMonth = calendar.get(Calendar.MONTH);
        tDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar dCalendar = Calendar.getInstance();
        dCalendar.set(dYear, dMonth, dDay);

        t = calendar.getTimeInMillis();
        d = dCalendar.getTimeInMillis();
        r = (d-t) / (24*60*60*1000);

        tem = false;

        resultNumber = (int) r + 1;

        newGoalBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
//                        Toast.makeText(getActivity().getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();
                dialog = (View) View.inflate(GoalActivity, R.layout.goal_doing_dialog, null);

                GoalVO newGoalData = new GoalVO();

                retrofitInterface.getNewGno().enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        temp_gno = response.body() + 1;
                        Toast.makeText(GoalActivity, temp_gno+"", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });


                edtTitle = (EditText) dialog.findViewById(R.id.edtGoalTitle);
                edtContent = (EditText) dialog.findViewById(R.id.edtGoalContent);
                dPicker = (DatePicker) dialog.findViewById(R.id.goalDatePicker);

                AlertDialog.Builder dlg = new AlertDialog.Builder(GoalActivity);

                dlg.setTitle("목표 추가");

                dPicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        tem = true;
                        dYear = year;
                        dMonth = monthOfYear;
                        dDay = dayOfMonth;
                        final Calendar dCalendar = Calendar.getInstance();
                        dCalendar.set(dYear, dMonth, dDay);


                        d = dCalendar.getTimeInMillis();
                        r = (d-t) / (24 * 60 * 60 * 1000);
                        exitOk = true;
                        resultNumber = (int) r;

                        temp_date = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);

//                        Log.d("선택 : ", temp_date);
                    }
                });
                dlg.setView(dialog);

                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        newGoalData.setGno(temp_gno);
//                        newGoalData.setGtitle(edtTitle.getText().toString());
//                        newGoalData.setGcontent(edtContent.getText().toString());
//                        if (tem == true) {
//                            if (resultNumber > 0) {
//                                newGoalData.setFinDate(temp_date);
//                                exitOk = true;
//                            } else if (resultNumber <= 0) {
//                                Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } else {
//                            Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        newGoalData.setSetDate(today);
//                        newGoalData.setMno(1);
//
//                        Toast.makeText(GoalActivity, temp_date+"", Toast.LENGTH_SHORT).show();
//=============================================================
//                        if (tem == true) {
//                            if (resultNumber > 0) {
//                                data.setDoingDday(String.format("D-%d",resultNumber));
//                            } else if (resultNumber <= 0) {
//                                Toast.makeText(getContext(), "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
////                                data.setDoingDday("D day");
////                            } else {
////                                int absR= Math.abs(resultNumber);
////                                data.setDoingDday(String.format("D+%d",absR));
////                            }
//                        } else {
//                            Toast.makeText(getContext(), "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
//                            return;
////                            data.setDoingDday("D day");
//                        }
//                        Toast.makeText(GoalActivity, tem + "" ,Toast.LENGTH_SHORT).show();
                        // 날짜 미 선택시 DatePicker는 현재 날짜로 설정되어있는데 메인에선 계산된 날짜 값이 출력 되므로,
                        // tem에 boolean값을 주어 날짜 미 선택시 반드시 D day가 나오도록 조건 설정

//                        retrofitInterface.registerGoal(newGoalData).enqueue(new Callback<GoalVO>() {
//                            @Override
//                            public void onResponse(Call<GoalVO> call, Response<GoalVO> response) {
//                                Toast.makeText(GoalActivity, "새 목표 입력", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<GoalVO> call, Throwable t) {
//                                Log.d("오류", t+"");
//
//                            }
//                        });
//
//                        adapter.addItem(newGoalData);
//
//                        adapter.notifyDataSetChanged();
//
//                        tem = false;

                    }
                });

                dlg.setNegativeButton("취소", null);

                final AlertDialog dia = dlg.create();

                dia.show();

                dia.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (exitOk) {
                            newGoalData.setGno(temp_gno);
                            newGoalData.setGtitle(edtTitle.getText().toString());
                            newGoalData.setGcontent(edtContent.getText().toString());
                            if (tem == true) {
                                if (resultNumber > 0) {
                                    newGoalData.setFinDate(temp_date);
                                    exitOk = true;
                                } else if (resultNumber <= 0) {
                                    Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            newGoalData.setSetDate(today);
                            newGoalData.setMno(mno);

                            Toast.makeText(GoalActivity, temp_date+"", Toast.LENGTH_SHORT).show();
                            retrofitInterface.registerGoal(newGoalData).enqueue(new Callback<GoalVO>() {
                                @Override
                                public void onResponse(Call<GoalVO> call, Response<GoalVO> response) {
                                    Toast.makeText(GoalActivity, "새 목표 입력", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<GoalVO> call, Throwable t) {
                                    Log.d("오류", t+"");

                                }
                            });

                            adapter.addItem(newGoalData);

                            adapter.notifyDataSetChanged();

                            tem = false;

                            dia.dismiss();
                        } else {
                            Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

//        init();
//
//        getData();

        return rootview;
    }

    private void init() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            GoalActivity = (Activity) context;
        }
    }




//    private void getData() {
//        List<String> listTitle = new ArrayList<>();
//
//        List<String> listContent = new ArrayList<>();
//
//        for (int i = 0; i < listTitle.size(); i++) {
//
//            Data data = new Data();
//            data.setDoingTitle(listTitle.get(i));
//            data.setDoingDday(listContent.get(i));
//
//            adapter.addItem(data);
//        }
//
//        adapter.notifyDataSetChanged();
//    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private List<GoalVO> listData;

        private RecyclerAdapter(List<GoalVO> listData) {
            this.listData = listData;
        }

        @NonNull
        @Override
        public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doing_item, parent, false);
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

            private TextView doingTitle, doingDate, myGno;
            private EditText doingContent;

            ItemViewHolder(View itemView) {
                super(itemView);

                doingTitle = itemView.findViewById(R.id.doingTitle);
                doingDate = itemView.findViewById(R.id.doingDate);
                myGno = itemView.findViewById(R.id.myGno);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity().getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();

                        GoalVO sel_data = listData.get(getAdapterPosition());
                        Toast.makeText(getActivity().getApplicationContext(), myGno.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialog = (View) View.inflate(GoalActivity, R.layout.goal_doing_dialog, null);

                        edtTitle = (EditText) dialog.findViewById(R.id.edtGoalTitle);
                        edtContent = (EditText) dialog.findViewById(R.id.edtGoalContent);
                        dPicker = (DatePicker) dialog.findViewById(R.id.goalDatePicker);
                        edtTitle.setText(doingTitle.getText().toString());
                        edtContent.setText(sel_data.getGcontent());
//                        edtContent.setText();

                        AlertDialog.Builder dlg = new AlertDialog.Builder(GoalActivity);

                        dlg.setTitle("목표 상세");

                        dPicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                tem = true;
                                dYear = year;
                                dMonth = monthOfYear;
                                dDay = dayOfMonth;
                                final Calendar dCalendar = Calendar.getInstance();
                                dCalendar.set(dYear, dMonth, dDay);

                                d = dCalendar.getTimeInMillis();
                                r = (d-t) / (24 * 60 * 60 * 1000);

                                temp_date2 = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);

                                resultNumber = (int) r;
                                exitOk = true;
                            }
                        });

                        dlg.setView(dialog);

                        dlg.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                sel_data.setGcontent(edtTitle.getText().toString());
//                                if (tem == true) {
//                                    if (resultNumber > 0) {
//                                        sel_data.setFinDate(temp_date);
//                                    } else if (resultNumber <= 0) {
//                                        Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    } else {
//                                        Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }
//                                }
////                                Toast.makeText(GoalActivity, tem + "" ,Toast.LENGTH_SHORT).show();
//
//                                listData.set(getAdapterPosition(), sel_data);
//
//                                adapter.notifyDataSetChanged();
//
//                                tem = false;
                            }
                        });

                        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                retrofitInterface.removeGoal(Integer.parseInt(myGno.getText().toString())).enqueue(new Callback<Void>() {
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

                        final AlertDialog dia = dlg.create();

                        dia.show();

                        dia.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (exitOk) {
                                    sel_data.setGtitle(edtTitle.getText().toString());
                                    sel_data.setGcontent(edtContent.getText().toString());
                                    if (tem == true) {
                                        if (resultNumber > 0) {
                                            sel_data.setFinDate(temp_date2);
                                            exitOk = true;
                                        } else if (resultNumber <= 0) {
                                            Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    Toast.makeText(GoalActivity, temp_date2+"", Toast.LENGTH_SHORT).show();
                                    retrofitInterface.modifyGoal(Integer.parseInt(myGno.getText().toString()), sel_data).enqueue(new Callback<GoalVO>() {
                                        @Override
                                        public void onResponse(Call<GoalVO> call, Response<GoalVO> response) {
                                            Toast.makeText(GoalActivity, "목표 수정", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<GoalVO> call, Throwable t) {
                                            Log.d("오류", t+"");

                                        }
                                    });

                                    listData.set(getAdapterPosition(), sel_data);

                                    adapter.notifyDataSetChanged();

                                    tem = false;
                                    dia.dismiss();
                                } else {
                                    Toast.makeText(GoalActivity, "현재 날짜 이후로 설정해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }

            void onBind(GoalVO data) {
                doingTitle.setText(data.getGtitle());
                doingDate.setText("종료 : " + data.getFinDate());
                myGno.setText(data.getGno()+"");
            }
        }
    }
}
