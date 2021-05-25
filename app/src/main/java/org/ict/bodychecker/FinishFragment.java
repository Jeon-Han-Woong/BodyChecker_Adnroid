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

public class FinishFragment extends Fragment {

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView2;
    private Activity GoalActivity;
    View dialog;
    EditText edtTitle, edtContent;
    DatePicker dPicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_finish, container, false);

        recyclerView2 = (RecyclerView) rootview.findViewById(R.id.recyclerView2);

        recyclerView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        init();

        getData();

        return rootview;
    }

    private void init() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView2.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView2.setAdapter(adapter);
    }




    private void getData() {
        List<String> listTitle = Arrays.asList("1", "2", "3", "1", "2", "3", "1", "2", "3");

        List<String> listContent = Arrays.asList("1a", "2b", "3c", "1a", "2b", "3c", "1a", "2b", "3c");

        for (int i = 0; i < listTitle.size(); i++) {

            Data2 data = new Data2();
            data.setFinishTitle(listTitle.get(i));

            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            GoalActivity = (Activity) context;
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private ArrayList<Data2> listData = new ArrayList<Data2>();

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

        void addItem(Data2 data) {
            listData.add(data);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView finishTitle, finishDate, myFinGno;

            ItemViewHolder(View itemView) {
                super(itemView);

                finishTitle = itemView.findViewById(R.id.finishTitle);
                finishDate = itemView.findViewById(R.id.finishDate);
                myFinGno = itemView.findViewById(R.id.myFinGno);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Data2 sel_data = listData.get(getAdapterPosition());

                        dialog = (View) View.inflate(GoalActivity, R.layout.goal_doing_dialog, null);

                        edtTitle = (EditText) dialog.findViewById(R.id.edtGoalTitle);
                        edtContent = (EditText) dialog.findViewById(R.id.edtGoalContent);
                        dPicker = (DatePicker) dialog.findViewById(R.id.goalDatePicker);

                        edtTitle.setEnabled(false);
                        edtContent.setEnabled(false);
                        dPicker.setEnabled(false);

                        edtTitle.setText(finishTitle.getText().toString());
                        edtContent.setText(sel_data.getFinishContent());


//                        edtContent.setText();

                        AlertDialog.Builder dlg = new AlertDialog.Builder(GoalActivity);

                        dlg.setTitle("목표 상세");

                        dlg.setView(dialog);

                        dlg.setPositiveButton("확인", null);

                        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "목표 \"" + edtTitle.getText().toString() + "\" 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                                listData.remove(getAdapterPosition());

                                adapter.notifyDataSetChanged();
                            }

                        });

                        dlg.show();
                    }
                });
            }

            void onBind(Data2 data) {
                finishTitle.setText(data.getFinishTitle()); }
        }
    }
}
