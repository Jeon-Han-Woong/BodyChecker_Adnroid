package org.ict.bodychecker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeDetailActivity extends AppCompatActivity {

    TextView noticeTitle, noticeWriteDate, noticeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail);

        Intent intent = getIntent();

        String title, writedate, content;
        title = intent.getStringExtra("title");
        writedate = intent.getStringExtra("writedate");
        content = intent.getStringExtra("content");

        noticeTitle = (TextView) findViewById(R.id.noticeTitle);
        noticeWriteDate = (TextView) findViewById(R.id.noticeWriteDate);
        noticeContent = (TextView) findViewById(R.id.noticeContent);

        noticeTitle.setText(title);
        noticeWriteDate.setText(writedate);
        noticeContent.setText(content);
    }
}
