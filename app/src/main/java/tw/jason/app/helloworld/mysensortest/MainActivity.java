package tw.jason.app.helloworld.mysensortest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    private MySensorListener listener;
    private TextView x, y,z;
    private MyView myView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myView = new MyView();

        setContentView(myView);
//        x = (TextView)findViewById(R.id.x);
//        y = (TextView)findViewById(R.id.y);
//        z = (TextView)findViewById(R.id.z);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors =sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor: sensors){
            String sensorName = sensor.getName();
            String sensorType = sensor.getStringType();
            Log.i("brad",sensorName+":"+sensorType);
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new MySensorListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(listener,sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    private class  MySensorListener implements SensorEventListener2{
        @Override
        public void onFlushCompleted(Sensor sensor) {

        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
//            x.setText("X:"+(int)(values[0]*100));
//            y.setText("Y:"+(int)(values[1]*100));
//            z.setText("Z:"+(int)(values[2]*100));
            myView.setXY(values[0]*-1, values[1]);


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
    private class  MyView extends View{
        private boolean isInit;
        private float viewW, viewH, ballX, ballY;
        private Paint paint, paintAxis;

        MyView(){
            super(MainActivity.this);
            setBackgroundColor(Color.BLACK);
            paint = new Paint();
            paint.setColor(Color.YELLOW);
            paintAxis = new Paint();
            paintAxis.setColor(Color.RED);
            paintAxis.setStrokeWidth(2);

        }

        private void init(){
            isInit = true;
            viewW = getWidth(); viewH = getHeight();
            ballX = viewW / 2f; ballY = viewH / 2f;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!isInit) init();

            canvas.drawCircle(ballX, ballY, 80, paint);
            canvas.drawLine(0,viewH/2f,viewW, viewH/2f, paintAxis);
            canvas.drawLine(viewW/2f, 0, viewW/2f, viewH, paintAxis);

        }

        void setXY(float x, float y){
            ballX = viewW/2f + (x * viewW/(2f * 9.8f));
            ballY = viewH/2f + (y * viewH/(2f * 9.8f));
            invalidate();
        }


    }


}

