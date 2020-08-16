package lc.test.case_fase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;

public class SafeGuideActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getX()>e2.getX()){
                    next(null);
                }else if(e1.getX()<e2.getX()){
                    prev(null);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void prev(View view) {
        overridePendingTransition(R.anim.translate_page_prev_right,R.anim.translate_page_prev_left);
    }

    public void next(View view) {
        overridePendingTransition(R.anim.translate_page_net_right,R.anim.translate_page_net_left);
    }
}
