package com.example.memes;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Button;



public class MainTab extends Fragment {


    private TextView textViewRealAngle;
    private TextView textViewRealWeight;
    private ImageButton infoImgBtn;
    private ImageButton settingsImgBtn;
    private ImageView rotateImg;
    private ImageView circleImg;

    /*Used for Accelometer & Gyroscoper*/
    private SensorManager mSensorManager = null;
    private UserSensorListener userSensorListener;
    private Sensor mGyroscopeSensor = null;
    private Sensor mAccelerometer = null;

    /*Sensor variables*/
    private float[] mGyroValues = new float[3];
    private float[] mAccValues = new float[3];
    private double mAccPitch, mAccRoll;

    /*for using complementary filter*/
    private float a = 0.2f;
    private static final float NS2S = 1.0f/1000000000.0f;

    private double pitch = 0, roll = 0;
    private double timestamp;
    private double dt;
    private double temp;

    private boolean gyroRunning;
    private boolean accRunning;

    private SharedPreferences mPref;
    private Toast mToast;
    //private int cnt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        textViewRealAngle = rootView.findViewById(R.id.textView_realAngle);
        textViewRealWeight=rootView.findViewById(R.id.textView_realWeight);

        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        userSensorListener = new UserSensorListener();
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometer= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(userSensorListener, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(userSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

        //Using the Gyroscope & Accelometer
        //1. 센서 매니저 생성 -> getSystemService를 이용
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //Using the Accelometer
        //2. 센서 객체 생성 -> getDefaultSensor(Sensor.원하는센서)
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //3. 센서 리스너 생성
        userSensorListener = new UserSensorListener();
        //mGyroLis = new GyroscopeListener();

        infoImgBtn = rootView.findViewById(R.id.imageButton_info);
        infoImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
            }
        });

        settingsImgBtn = rootView.findViewById(R.id.imageButton_settings);
        settingsImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        circleImg = rootView.findViewById(R.id.imageView01);
        rotateImg = rootView.findViewById(R.id.imageView02);
        get_alarmMethodList_PreferencesData();
        get_popupLocationList_PreferencesData();
        mToast = Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT);


        return rootView;
    }



    @Override
    public void onResume(){
        super.onResume();
        //4. 센서 리스너에 등록 -> 센서매니저.registerListener(센서리스너클래스,센서객체,리스너반응속도)
        //반응속도 빠른 순서: SENSOR_DELAY_FASTEST,GAME,UI,NORMAL
        mSensorManager.registerListener(userSensorListener, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(userSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        get_alarmMethodList_PreferencesData();
        get_popupLocationList_PreferencesData();
    }

    @Override
    public void onPause(){
        super.onPause();
        //5. 센서 리스너 등록 해제
        mSensorManager.unregisterListener(userSensorListener);
    }

    /* 1차 상보필터 적용 메서드 */
    private void complementaty(double new_ts){

        /* 자이로랑 가속 해제 */
        gyroRunning = false;
        accRunning = false;

        /*센서 값 첫 출력시 dt(=timestamp - event.timestamp)에 오차가 생기므로 처음엔 break */
        if(timestamp == 0){
            timestamp = new_ts;
            return;
        }
        dt = (new_ts - timestamp) * NS2S; // ns->s 변환
        timestamp = new_ts;

        /* degree measure for accelerometer */
        mAccPitch = -Math.atan2(mAccValues[0], mAccValues[2]) * 180.0 / Math.PI; // Y 축 기준
        mAccRoll= Math.atan2(mAccValues[1], mAccValues[2]) * 180.0 / Math.PI; // X 축 기준

        /**
         * 1st complementary filter.
         *  mGyroValuess : 각속도 성분.
         *  mAccPitch : 가속도계를 통해 얻어낸 회전각.
         */
        temp = (1/a) * (mAccPitch - pitch) + mGyroValues[1];
        pitch = pitch + (temp*dt);

        temp = (1/a) * (mAccRoll - roll) + mGyroValues[0];
        roll = roll + (temp*dt);

        textViewRealAngle.setText(""+roll);

        int alarmMethod_index = get_alarmMethodList_PreferencesData();
        int popupMethod_index = get_popupLocationList_PreferencesData();
        int cnt=0;

        if(roll>=75.0&&roll<=90.0)
        {
            textViewRealAngle.setText("0°~15°");
            textViewRealWeight.setText("4.5KG");
            rotateImg.setImageResource(R.drawable.zero15);
            circleImg.setImageResource(R.drawable.back015);
        }
        else if(roll>=60.0&&roll<75.0)
        {
            textViewRealAngle.setText("15°~30°");
            textViewRealWeight.setText("12KG");
            rotateImg.setImageResource(R.drawable.fifteen30);
            circleImg.setImageResource(R.drawable.back1530);
        }
        else if(roll>=45.0&&roll<60.0)
        {
            textViewRealAngle.setText("30°~45°");
            textViewRealWeight.setText("18KG");
            rotateImg.setImageResource(R.drawable.thirty45);
            circleImg.setImageResource(R.drawable.back3045);
        }
        else if(roll>=30.0&&roll<45.0)
        {
            textViewRealAngle.setText("45°~60°");
            textViewRealWeight.setText("22KG");
            rotateImg.setImageResource(R.drawable.fortyfive60);
            circleImg.setImageResource(R.drawable.back4560);
        }
        else if(roll>=0.0&&roll<30.0)
        {
            textViewRealAngle.setText("60°~90°");
            textViewRealWeight.setText("26KG");
            rotateImg.setImageResource(R.drawable.sixty90);
            circleImg.setImageResource(R.drawable.back6090);
            if(alarmMethod_index==0)//알림 방법:팝업(무음)
            {
                cnt+=1;
                if(cnt<=1){
                    if(popupMethod_index==0){//팝업 위치: 상단
                        mToast.setGravity(Gravity.TOP,0,0);
                    }
                    else if(popupMethod_index==1){//팝업 위치: 중단
                        mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    }
                    else if(popupMethod_index==2){//팝업 위치: 하단
                        mToast.setGravity(Gravity.BOTTOM,0,0);
                    }
                    mToast.setText("거북목입니다!");
                    mToast.show();
                }
                else{
                    mToast.cancel();
                }
            }
            else if(alarmMethod_index==1)//알림 방법: 진동
            {
                cnt+=1;
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                if(cnt<=1) {
                    new Thread(new Runnable() {
                        public void run() {
                            ((Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500); //
                        }
                    }).start();
                }
                else{
                    vibrator.cancel();
                }
            }
            else if(alarmMethod_index==2)//알림 방법: 팝업과 진동
            {
                cnt+=1;
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                if(cnt<=1){
                    if(popupMethod_index==0){//팝업 위치: 상단
                        mToast.setGravity(Gravity.TOP,0,0);
                    }
                    else if(popupMethod_index==1){//팝업 위치: 중단
                        mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    }
                    else if(popupMethod_index==2){//팝업 위치: 하단
                        mToast.setGravity(Gravity.BOTTOM,0,0);
                    }
                    mToast.setText("거북목입니다!");
                    mToast.show();
                    new Thread(new Runnable() {
                        public void run() {
                            ((Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500); //
                        }
                    }).start();
                }
                else{
                    mToast.cancel();
                    vibrator.cancel();
                }
            }
        }
        else if(roll>90.0)
        {
            textViewRealAngle.setText("~0°");
            textViewRealWeight.setText("누워계신가요?");
            rotateImg.setImageResource(R.drawable.zero15);
            circleImg.setImageResource(R.drawable.back0under);
        }
        else
        {
            textViewRealAngle.setText("90°~");
            textViewRealWeight.setText("27KG 이상!");
            rotateImg.setImageResource(R.drawable.ninetyover);
            circleImg.setImageResource(R.drawable.back90over);
            if(alarmMethod_index==0)//알림 방법:팝업(무음)
            {
                cnt+=1;
                if(cnt<=1){
                    if(popupMethod_index==0){//팝업 위치: 상단
                        mToast.setGravity(Gravity.TOP,0,0);
                    }
                    else if(popupMethod_index==1){//팝업 위치: 중단
                        mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    }
                    else if(popupMethod_index==2){//팝업 위치: 하단
                        mToast.setGravity(Gravity.BOTTOM,0,0);
                    }
                    mToast.setText("거북목입니다!");
                    mToast.show();
                }
                else
                {
                    mToast.cancel();
                }
            }
            else if(alarmMethod_index==1)//알림 방법: 진동
            {
                cnt+=1;
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                if(cnt<=1)
                {
                    new Thread(new Runnable() {
                        public void run() {
                            ((Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500); //
                        }
                    }).start();
                }
                else
                {
                    vibrator.cancel();
                }
            }
            else if(alarmMethod_index==2)//알림 방법: 팝업과 진동
            {
                cnt+=1;
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                if(cnt<=1)
                {
                    if(popupMethod_index==0)//팝업 위치: 상단
                    {
                        mToast.setGravity(Gravity.TOP,0,0);
                    }
                    else if(popupMethod_index==1)//팝업 위치: 중단
                    {
                        mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    }
                    else if(popupMethod_index==2)//팝업 위치: 하단
                    {
                        mToast.setGravity(Gravity.BOTTOM,0,0);
                    }
                    mToast.setText("거북목입니다!");
                    mToast.show();
                    new Thread(new Runnable() {
                        public void run() {
                            ((Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500); //
                        }
                    }).start();
                }
                else
                {
                    mToast.cancel();
                    vibrator.cancel();
                }
            }
        }
    }

    private int get_alarmMethodList_PreferencesData() {
        String[] array = getResources().getStringArray(R.array.alarmMethodArray);
        int index = getArrayIndex(R.array.alarmMethodArray_values,mPref.getString("alarmMethodList","popup"));
        return index;
    }

    private int get_popupLocationList_PreferencesData() {
        String[] array = getResources().getStringArray(R.array.alarmMethodArray);
        int index = getArrayIndex(R.array.popupLocationArray_values,mPref.getString("popupLocationList","top"));
        return index;
    }

    private int getArrayIndex(int array, String findIndex) {
        String[] arrayString = getResources().getStringArray(array);
        for (int e = 0; e < arrayString.length; e++) {
            if (arrayString[e].equals(findIndex))
                return e;
        }
        return -1;
        }

    public class UserSensorListener implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()){

                /** GYROSCOPE */
                case Sensor.TYPE_GYROSCOPE:

                    /*센서 값을 mGyroValues에 저장*/
                    mGyroValues = event.values;
                    if(!gyroRunning)
                        gyroRunning = true;

                    /*센서 값을 mGyroValues에 저장*/
                    mGyroValues = event.values;

                    if(!gyroRunning)
                        gyroRunning = true;

                    break;

                /** ACCELEROMETER */
                case Sensor.TYPE_ACCELEROMETER:

                    /*센서 값을 mAccValues에 저장*/
                    mAccValues = event.values;
                    if(!accRunning)
                        accRunning = true;
                    break;
            }

            /**두 센서 새로운 값을 받으면 상보필터 적용*/
            if(gyroRunning && accRunning){
                complementaty(event.timestamp);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    }


    public class Toggle_Activity extends AppCompatActivity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_main);
            final ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tb.isChecked()) {
                        Intent intent = new Intent(Toggle_Activity.this, MyService.class);
                        StopService(intent);

                    } else {
                        Intent intent = new Intent(Toggle_Activity.this, MyService.class);
                        StartService(intent);
                    }
                }}

            );
        }
        }

    }
}