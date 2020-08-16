package lc.test.case_fase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.Encrypt;
import lc.test.case_fase.util.SpTools;

public class HomeActivity extends AppCompatActivity {

    private String[] mItemNames={"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
    private int[] mItemSources={R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,R.drawable.home_taskmanager,R.drawable.home_netmanager
            , R.drawable.home_trojan,R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};
    private GridView gv_items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gv_items=findViewById(R.id.gv_items);
        putItems();
    }

    private void putItems(){
        gv_items.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItemNames.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(getApplicationContext(), R.layout.item_home_item, null);
                ImageView iv_icon=view.findViewById(R.id.iv_icon);
                TextView tv_name=view.findViewById(R.id.tv_name);
                iv_icon.setImageResource(mItemSources[position]);
                tv_name.setText(mItemNames[position]);
                return view;
            }
        });
        gv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        checkSafePassword();
                        break;
                    case 8:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        break;
                }
            }
        });
    }

    private void checkSafePassword(){
        String pwd = SpTools.getString(this, ConstantValue.SAFE_PASSWORD, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        View container;
        if(TextUtils.isEmpty(pwd)){
            container=View.inflate(this,R.layout.modal_safe_pwd1,null);
            Button btn_confirm=container.findViewById(R.id.btn_confirm);
            EditText et_pwd1=container.findViewById(R.id.et_pwd1);
            EditText et_pwd2=container.findViewById(R.id.et_pwd2);
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd1 = et_pwd1.getText().toString();
                    String pwd2 = et_pwd2.getText().toString();
                    if(TextUtils.isEmpty(pwd1)){
                        Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(pwd2) || !pwd2.equals(pwd1)){
                        Toast.makeText(getApplicationContext(),"确认密码不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SpTools.putString(getApplicationContext(),ConstantValue.SAFE_PASSWORD, Encrypt.md5(pwd1));
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),SafeActivity.class));
                }
            });
            Button btn_cancel=container.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else{
            container=View.inflate(this,R.layout.modal_safe_pwd2,null);
            Button btn_confirm=container.findViewById(R.id.btn_confirm);
            EditText et_pwd=container.findViewById(R.id.et_pwd);
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pwd.equals(Encrypt.md5(et_pwd.getText().toString()))){
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),SafeActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Button btn_cancel=container.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.setView(container);
        dialog.show();
    }
}
