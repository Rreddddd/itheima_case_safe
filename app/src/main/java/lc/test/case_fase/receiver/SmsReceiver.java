package lc.test.case_fase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            Bundle extras = intent.getExtras();
            if(extras!=null){
                Object[] pdus = (Object[]) extras.get("pdus");
                if(pdus!=null){
                    StringBuilder sb=new StringBuilder();
                    for(Object pdu : pdus){
                        SmsMessage.createFromPdu((byte[]) pdu);
                        sb.append(new String((byte[]) pdu));
                    }
                    if(sb.toString().contains("#*location*#")){
                        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Log.i("SmsReceiver","位置改变");
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                Log.i("SmsReceiver","开关变化");
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                Log.i("SmsReceiver","位置可用");
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                Log.i("SmsReceiver","位置不可用");
                            }
                        });
//                        SmsManager.getDefault().sendTextMessage("5556",null,"",null,null);
                    }
                }
            }
        }
    }
}
