package lc.test.case_fase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String simSerial = SpTools.getString(context, ConstantValue.SAFE_SIM_SERIAL, "");
        boolean safeEnabled = SpTools.getBoolean(context, ConstantValue.SAFE_ENABLED, false);
        if(safeEnabled && !TextUtils.isEmpty(simSerial)){
            simSerial+="123";
            TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(!simSerial.equals(telephonyManager.getSimSerialNumber())){
                String number = SpTools.getString(context, ConstantValue.SAFE_CONTACT_NUMBER, "");
                if(!TextUtils.isEmpty(number)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number,null,"sim change!!!!!",null,null);
                }
            }
        }
    }
}
