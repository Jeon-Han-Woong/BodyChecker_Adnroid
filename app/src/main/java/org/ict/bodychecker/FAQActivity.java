package org.ict.bodychecker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FAQActivity extends AppCompatActivity {

    ListView faqList;
    String[] ask = {"BMI수치는 어떻게 계산되는 건가요?", "칼로리 소모량은 어떻게 계산되는 건가요?", ""};
    String[] answer = {"BMI수치의 계산식은 ( 체중 ÷ 키² )으로 계산됩니다.", "운동 종류에 따라 다르게 계산되며\n걷기의 경우 ( 60kg × 10분 ) = 약 40kcal입니다."};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);

        faqList = (ListView) findViewById(R.id.faqList);

        AlertDialog.Builder dlg = new AlertDialog.Builder(FAQActivity.this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ask);
        faqList.setAdapter(adapter);

        faqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dlg.setTitle(ask[i]);
                dlg.setMessage(answer[i]);
                dlg.setNegativeButton("닫기", null);
                dlg.show();
            }
        });
    }
}
