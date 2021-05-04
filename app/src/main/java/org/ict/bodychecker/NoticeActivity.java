package org.ict.bodychecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    ListView noticeList;

    ArrayList<String> nlist = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        for(int i=0; i<20; i++) {
            nlist.add("공지사항" + (i+1));
        }

        noticeList = (ListView) findViewById(R.id.noticeList);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nlist);
        noticeList.setAdapter(adapter);

        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoticeDetailActivity.class);
                intent.putExtra("title", "공지사항 제목"+i);
                intent.putExtra("writedate", "2021.05.04");
                intent.putExtra("content", "공지사항 본문");
                startActivity(intent);
            }
        });
    }
}
