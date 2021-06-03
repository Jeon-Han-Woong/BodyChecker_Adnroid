package org.ict.bodychecker.StepService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackGroundWalking extends Service implements SensorEventListener {

    private MyBinder myBinder = new MyBinder();

    class MyBinder extends Binder {
        BackGroundWalking getService() {
            return BackGroundWalking.this;
        }//getService
    }//MyBinder

    private int stepDetector;
    private StepCallBack callBack;

    public void setCallBack(StepCallBack callBack) {
        this.callBack = callBack;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }//onBind

    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }//onCreate

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        return super.onStartCommand(intent, flags, startId);
    }//onStartCommand

    @Override
    public boolean onUnbind(Intent intent) {
        unRegistManager();
        if (callBack != null)
            callBack.onUnbindService();
        return super.onUnbind(intent);
    }

    public void unRegistManager() { //혹시 모를 에러상황에 트라이 캐치
        try {
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (sensorEvent.values[0] == 1.0f) {
                stepDetector += sensorEvent.values[0];
                if (callBack != null)
                    callBack.onStepCallBack(stepDetector);
                Log.e("스텝 디텍터", "" + sensorEvent.values[0]);
            }//if
        }//if
    }//onSensorChanged

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }//onAccuracyChanged
}
