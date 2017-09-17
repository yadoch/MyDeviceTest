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
    private TelephonyManager tngr;
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
        tngr=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String deviceid = tngr.getDeviceId();
        Log.i("brad","IMEI:"+deviceid);
        String num =tngr.getLine1Number();
        Log.i("brad","phone:"+num);
        String IMSI=tngr.getSubscriberId();
        Log.i("brad","IMSI:"+IMSI);

        tngr.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }
    //
    private class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i("brad","IDLE");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i("brad","RING:"+incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("brad","offhook");
                    break;
            }
        }
    }
}
