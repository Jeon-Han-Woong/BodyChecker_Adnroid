package org.ict.bodychecker.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ict.bodychecker.R;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    ListView noticeList;

    String[] ntitle = {"확찐자 길들이기 서비스 시작했습니다!", "별점 이벤트 진행!", "문의 주소입니다."};
    String[] ndate = {"2021-06-01", "2021-06-02", "2021-06-03"};
    String[] ncontent = {"확찐자 길들이기 정식 서비스 시작했습니다! 많은 사랑부탁드립니다이~",
            "별점을 남겨주시고 이메일을 보내주세요! 추첨을 통해 문화상품권을 드립니다~",
            "문의 주소는 qwer@asdf.com, 연락처는 010-9876-1234 입니다."};

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_exer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noticeList = (ListView) findViewById(R.id.noticeList);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ntitle);
        noticeList.setAdapter(adapter);

        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoticeDetailActivity.class);
                intent.putExtra("title", ntitle[i]);
                intent.putExtra("writedate", ndate[i]);
                intent.putExtra("content", ncontent[i]);
                startActivity(intent);
            }
        });
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
