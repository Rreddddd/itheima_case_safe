package lc.test.case_fase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;
import lc.test.case_fase.view.SettingItemView;

public class SafeGuide2Activity extends SafeGuideActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_guide2);

        SettingItemView settingItemView=findViewById(R.id.stv_bind_card);
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingItemView.toggleState();
                getSIMSerial(settingItemView.getState());
            }
        });
        String simSerial = SpTools.getString(this, ConstantValue.SAFE_SIM_SERIAL, "");
        if(!TextUtils.isEmpty(simSerial)){
            settingItemView.setState(true);
        }else{
            settingItemView.setState(false);
        }
    }

    private void getSIMSerial(boolean state){
        if(state){
            TelephonyManager telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            if(!TextUtils.isEmpty(simSerialNumber)){
                SpTools.putString(this,ConstantValue.SAFE_SIM_SERIAL,simSerialNumber);
            }
        }else{
            SpTools.remove(this, ConstantValue.SAFE_SIM_SERIAL);
        }
    }

    public void prev(View view) {
        startActivity(new Intent(this,SafeGuide1Activity.class));
        finish();

        super.prev(view);
    }

    public void next(View view) {
//        if(TextUtils.isEmpty(SpTools.getString(this,ConstantValue.SAFE_SIM_SERIAL,""))){
//            Toast.makeText(this,"需要绑定sim卡",Toast.LENGTH_SHORT).show();
//            return;
//        }
        startActivity(new Intent(this,SafeGuide3Activity.class));
        finish();

        super.next(view);
    }
}
