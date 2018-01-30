package eecs40.tamagotchi;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public static Context context;
    Sensor accelerometer;
    SensorManager senseAccelerometer;
    long updateTime = 0;
    float x, y, acceleration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(new TamaView(this));
        senseAccelerometer = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = senseAccelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senseAccelerometer.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static void saveData(Context context){
        SharedPreferences data = context.getSharedPreferences("TamaData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("heartCount", TamaView.p.hearts);
        editor.putInt("fitnessCount", TamaView.p.steps);
        editor.putLong("timeCount", TamaView.offTime);
        editor.putInt("x1", TamaView.p.x1);
        editor.putInt("y1", TamaView.p.y1);
        editor.putInt("x2", TamaView.p.x2);
        editor.putInt("y2", TamaView.p.y2);
        editor.commit();
    }

    public static void loadData(Context context){
        SharedPreferences data = context.getSharedPreferences("TamaData", Context.MODE_PRIVATE);
        TamaView.p.hearts = data.getInt("heartCount", 5);
        TamaView.p.steps = data.getInt("fitnessCount", TamaView.fullFitness/2);
        TamaView.offTime = data.getLong("timeCount", System.currentTimeMillis());
        TamaView.p.x1 = data.getInt("x1", 400);
        TamaView.p.y1 = data.getInt("y1", 400);
        TamaView.p.x2 = data.getInt("x2", 700);
        TamaView.p.y2 = data.getInt("y2", 700);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor acc = event.sensor;
        if (acc.getType() == Sensor.TYPE_ACCELEROMETER){
            if ((System.currentTimeMillis() - updateTime) > 100){
                acceleration = Math.abs(event.values[0] + event.values[1]- x - y)/(System.currentTimeMillis() - updateTime);
                if (acceleration > .1){
                    TamaView.p.incStep();
                }
                x = event.values[0];
                y = event.values[1];

                updateTime = System.currentTimeMillis();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}