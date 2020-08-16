package lc.test.case_fase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;

public class SafeGuide3Activity extends SafeGuideActivity {

    private EditText et_phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_guide3);

        et_phone_number=findViewById(R.id.et_phone_number);
        et_phone_number.setText(SpTools.getString(this,ConstantValue.SAFE_CONTACT_NUMBER,""));
        et_phone_number.setSelection(et_phone_number.getText().length());
        findViewById(R.id.btn_select_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),ContactListActivity.class),0);
            }
        });
    }

    public void prev(View view) {
        startActivity(new Intent(this,SafeGuide2Activity.class));
        finish();

        super.prev(view);
    }

    public void next(View view) {
        String phoneNumber=et_phone_number.getText().toString().trim().replaceAll("\\D","");
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"选择对应号码",Toast.LENGTH_SHORT).show();
            return;
        }
        SpTools.putString(this, ConstantValue.SAFE_CONTACT_NUMBER,phoneNumber);
        startActivity(new Intent(this,SafeGuide4Activity.class));
        finish();

        super.next(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null){
            String number = data.getStringExtra("number");
            if(number==null){
                number="";
            }else{
                number=number.replaceAll("\\D","");
            }
            et_phone_number.setText(number);
            et_phone_number.setSelection(et_phone_number.getText().length());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
