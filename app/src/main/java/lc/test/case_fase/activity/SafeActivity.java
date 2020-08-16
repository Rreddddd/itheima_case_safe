package lc.test.case_fase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;

public class SafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SpTools.getBoolean(this, ConstantValue.SAFE_GUIDE_STATE,false)){
            setContentView(R.layout.activity_safe);

            ImageView tv_lock=findViewById(R.id.tv_lock);
            if(SpTools.getBoolean(this,ConstantValue.SAFE_ENABLED,false)){
                tv_lock.setImageResource(R.drawable.lock);
            }else{
                tv_lock.setImageResource(R.drawable.unlock);
            }
            findViewById(R.id.rl_re_enter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),SafeGuide1Activity.class));
                }
            });
        }else{
            startActivity(new Intent(this,SafeGuide1Activity.class));
            finish();
        }
    }
}
