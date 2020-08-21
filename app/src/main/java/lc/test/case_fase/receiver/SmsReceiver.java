package lc.test.case_fase.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import lc.test.case_fase.R;

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
                        sb.append(SmsMessage.createFromPdu((byte[]) pdu).getMessageBody());
                    }
                    String textMsg=sb.toString();
                    if(textMsg.contains("#*location*#")){
                        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria,true), 0, 0, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Log.i("SmsReceiver","位置改变");
                                SmsManager.getDefault().sendTextMessage("5556",null,"location change\nlongitude:"+location.getLongitude()+"\nlatitude:"+location.getLatitude(),null,null);
                                locationManager.removeUpdates(this);
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
                    }
                    if(textMsg.contains("#*alarm*#")){
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.hint);
                        mediaPlayer.start();
                    }
                    DevicePolicyManager devicePolicyManager = enableDeviceManager(context);
                    if(devicePolicyManager!=null){
                        if(textMsg.contains("#*wipedata*#")){
                            enableDeviceManager(context);
                            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                        }
                        if(textMsg.contains("#*lockscreen*#")){
                            devicePolicyManager.lockNow();
                        }
                    }
                }
            }
        }
    }

    private DevicePolicyManager enableDeviceManager(Context context){
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if(devicePolicyManager!=null){
            ComponentName componentName=new ComponentName(context,DeviceReceiver.class);
            if(!devicePolicyManager.isAdminActive(componentName)){
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"aaaaaa");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        return devicePolicyManager;
    }
}
