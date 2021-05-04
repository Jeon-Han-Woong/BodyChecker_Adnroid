package org.ict.bodychecker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyInfoActivity extends AppCompatActivity {

    RadioGroup gendarRG;
    EditText edtName, edtHeight, edtWeight;
    DatePicker birthDP;
    Button modifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_info);

        gendarRG = (RadioGroup) findViewById(R.id.gendarRG);
        edtName = (EditText) findViewById(R.id.edtName);
        edtHeight = (EditText) findViewById(R.id.edtHeight);
        edtWeight = (EditText) findViewById(R.id.edtWeight);
        birthDP = (DatePicker) findViewById(R.id.birthDP);
        modifyBtn = (Button) findViewById(R.id.modifyBtn);

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "";
                if(gendarRG.getCheckedRadioButtonId() == R.id.male) str += "남자";
                else if(gendarRG.getCheckedRadioButtonId() == R.id.female) str += "여자";
                str += edtName.getText().toString() + edtHeight.getText().toString() + edtWeight.getText().toString();
                str += String.valueOf(birthDP.getYear()) + "0" + String.valueOf(birthDP.getMonth()+1) + String.valueOf(birthDP.getDayOfMonth());

                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
