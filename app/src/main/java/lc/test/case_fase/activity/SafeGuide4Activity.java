package lc.test.case_fase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;

public class SafeGuide4Activity extends SafeGuideActivity {

    private OnClickListener onClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_guide4);

        LinearLayout ll_checkbox=findViewById(R.id.ll_checkbox);
        onClickListener = new OnClickListener(ll_checkbox);
        onClickListener.setState(SpTools.getBoolean(this, ConstantValue.SAFE_ENABLED,false));
        ll_checkbox.setOnClickListener(onClickListener);
    }

    public void prev(View view) {
        startActivity(new Intent(this,SafeGuide3Activity.class));
        finish();

        super.prev(view);
    }

    public void next(View view) {
        if(!onClickListener.getState()){
            Toast.makeText(this,"勾选开启",Toast.LENGTH_SHORT).show();
            return;
        }
        SpTools.putBoolean(this, ConstantValue.SAFE_ENABLED,true);
        SpTools.putBoolean(this, ConstantValue.SAFE_GUIDE_STATE,true);
        startActivity(new Intent(this,SafeActivity.class));
        finish();
    }

    private static class OnClickListener implements View.OnClickListener{

        private CheckBox cb_enabled;
        private TextView tv_des;

        private OnClickListener(LinearLayout container){
            cb_enabled=container.findViewById(R.id.cb_enabled);
            tv_des=container.findViewById(R.id.tv_des);
        }

        @Override
        public void onClick(View v) {
            setState(!getState());
        }

        private void setState(boolean state){
            cb_enabled.setChecked(state);
            if(state){
                tv_des.setText("已开启防盗保护");
            }else{
                tv_des.setText("你没有开启防盗保护");
            }
        }

        public boolean getState(){
            return cb_enabled.isChecked();
        }
    }
}
