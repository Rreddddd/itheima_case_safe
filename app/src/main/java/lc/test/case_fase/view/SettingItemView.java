package lc.test.case_fase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lc.test.case_fase.R;

public class SettingItemView extends RelativeLayout {

    private TextView tv_des;
    private CheckBox cb_state;
    private String itemDesOnStr;
    private String itemDesOffStr;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_setting_item,this);
        TextView tv_title=findViewById(R.id.tv_title);
        String NAMESPACE = "http://schemas.android.com/apk/res-auto";
        tv_title.setText(attrs.getAttributeValue(NAMESPACE,"itemTitle"));
        itemDesOnStr=attrs.getAttributeValue(NAMESPACE,"itemDesOn");
        itemDesOffStr=attrs.getAttributeValue(NAMESPACE,"itemDesOff");
        tv_des=findViewById(R.id.tv_des);
        cb_state=findViewById(R.id.cb_state);
    }

    public void toggleState(){
        setState(!getState());
    }

    public void setState(boolean state){
        if(state){
            enable();
        }else{
            disable();
        }
    }

    public boolean getState(){
        return cb_state.isChecked();
    }

    public void enable(){
        tv_des.setText(itemDesOnStr);
        cb_state.setChecked(true);
    }

    public void disable(){
        tv_des.setText(itemDesOffStr);
        cb_state.setChecked(false);
    }
}
