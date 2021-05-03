package org.ict.bodychecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoingFragment extends Fragment {

    private Activity GoalActivity;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    View dialog;

    EditText edtTitle, edtContent;
    DatePicker dPicker;
    LinearLayout newGoalBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_doing, container, false);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);

        newGoalBtn = (LinearLayout) rootview.findViewById(R.id.newGoalBtn);

        newGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        Toast.makeText(getActivity().getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();
                dialog = (View) View.inflate(GoalActivity, R.layout.goal_doing_dialog, null);

                edtTitle = (EditText) dialog.findViewById(R.id.edtGoalTitle);
                edtContent = (EditText) dialog.findViewById(R.id.edtGoalContent);


                AlertDialog.Builder dlg = new AlertDialog.Builder(GoalActivity);

                dlg.setTitle("목표 추가");

                dlg.setView(dialog);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                dlg.setNegativeButton("취소", null);

                dlg.show();
            }
        });

        init();

        getData();

        return rootview;
    }

    private void init() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            GoalActivity = (Activity) context;
        }
    }




    private void getData() {
        List<String> listTitle = Arrays.asList("1", "2", "3", "1", "2", "3", "1", "2", "3");

        List<String> listContent = Arrays.asList("1a", "2b", "3c", "1a", "2b", "3c", "1a", "2b", "3c");

        for (int i = 0; i < listTitle.size(); i++) {

            Data data = new Data();
            data.setDoingTitle(listTitle.get(i));
            data.setDoingDday(listContent.get(i));

            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private ArrayList<Data> listData = new ArrayList<Data>();

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

        void addItem(Data data) {
            listData.add(data);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView doingTitle, doingDday;

            ItemViewHolder(View itemView) {
                super(itemView);

                doingTitle = itemView.findViewById(R.id.doingTitle);
                doingDday = itemView.findViewById(R.id.doingDday);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity().getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();
                        dialog = (View) View.inflate(GoalActivity, R.layout.goal_doing_dialog, null);

                        edtTitle = (EditText) dialog.findViewById(R.id.edtGoalTitle);
                        edtContent = (EditText) dialog.findViewById(R.id.edtGoalContent);

                        edtTitle.setText(doingTitle.getText().toString());
//                        edtContent.setText();

                        AlertDialog.Builder dlg = new AlertDialog.Builder(GoalActivity);

                        dlg.setTitle("목표 상세");

                        dlg.setView(dialog);

                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                doingTitle.setText(edtTitle.getText().toString());
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

            void onBind(Data data) {
                doingTitle.setText(data.getDoingTitle());
                doingDday.setText(data.getDoingDday());
            }
        }
    }
}
