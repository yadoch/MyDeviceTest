package tw.com.abc.mydevicetest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private TelephonyManager tmgr;
    private MyListener myListener;
    private int lastState=-1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        tmgr=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        String deviceid = tmgr.getDeviceId();
        Log.i("brad","IMEI:"+deviceid);
        String num =tmgr.getLine1Number();
        Log.i("brad","phone:"+num);
        String IMSI=tmgr.getSubscriberId();
        Log.i("brad","IMSI:"+IMSI);

        myListener=new MyListener();
        // 要先listen
        //tmgr.listen(myListener, PhoneStateListener.LISTEN_CALL_STATE);
        reListen();
    }



    //
    private class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //放上面和放下面看起來沒差異
            // super.onCallStateChanged(state, incomingNumber);
            Log.i("brad","get it!!");
            switch (state){
                //斷線狀態
                case TelephonyManager.CALL_STATE_IDLE:
                    if(state != lastState){
                        Log.i("brad","IDLE");
                        lastState = state;
                    }
                    break;
                //來電
                case TelephonyManager.CALL_STATE_RINGING:
                    if(state != lastState) {
                        Log.i("brad", "RING:" + incomingNumber);
                        // 可以不用再呼叫
                       // reListen();
                        lastState =state;
                    }
                    break;
                // 接電話
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("brad","offhook");
                    break;
                default:
                    Log.i("brad","other:"+state);
            }
            //放上面和放下面看起來沒差異
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    private void reListen(){
        tmgr.listen(myListener,PhoneStateListener.LISTEN_CALL_STATE);
    }
}
