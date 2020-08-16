package lc.test.case_fase.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;
import lc.test.case_fase.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView siv_update;
    private SettingItemView siv_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        siv_update=findViewById(R.id.siv_update);
        siv_update.setState(SpTools.getBoolean(getApplicationContext(),ConstantValue.SETTING_UPDATE_STATE,false));
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_update.toggleState();
                SpTools.putBoolean(getApplicationContext(),ConstantValue.SETTING_UPDATE_STATE,siv_update.getState());
            }
        });
        siv_location=findViewById(R.id.siv_location);
        siv_location.setState(SpTools.getBoolean(getApplicationContext(), ConstantValue.SETTING_LOCATION_STATE,false));
        siv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_location.toggleState();
                SpTools.putBoolean(getApplicationContext(),ConstantValue.SETTING_LOCATION_STATE,siv_location.getState());
            }
        });
    }
}
