package org.ict.bodychecker.StepService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.ict.bodychecker.ValueObject.DailyVO;
import org.ict.bodychecker.retrofit.RetrofitClient;
import org.ict.bodychecker.retrofit.RetrofitInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BackGroundWalking extends Service implements SensorEventListener {

    RetrofitClient retrofitClient;
    RetrofitInterface retrofitInterface;

    SensorManager sensorManager;
    Sensor stepDetectorSensor;

    private static int mno;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);

        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);

        if(intent != null) {
            mno = intent.getIntExtra("mno", 0);
            Log.d("Service_mno", String.valueOf(mno));
        }//if

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.values[0] == 1.0f) {
            addWalk(mno);
        }//if
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void addDaily(int mno) {
        DailyVO dailyVO = new DailyVO();
        dailyVO.setMno(mno);
        dailyVO.setDdate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));

        retrofitInterface.addDaily(dailyVO).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() == "SUCCESS") {
                    Log.d("addDaily", "daily 테이블 생성 완료");
                } else {
                    Log.d("addDaily", "에러발생");
                    return;
                }//else
            }//onResponse

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("addDaily", "addDaily Fail");
//                t.printStackTrace();
            }//onFailure
        });//addDaily
    }//addDaily

    private void addWalk(int mno) {
        retrofitInterface.addWalk(mno).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }//onResponse

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("countWalk", "countWalk Fail");
                Log.d("countWalk", "Daily 테이블에 정보가 없어 발생할 가능성 높음");
                addDaily(mno);
            }//onFailure

        });//countWalk
    }//getWalk

}//Service
