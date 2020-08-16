package lc.test.case_fase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import lc.test.case_fase.R;

public class SafeGuide1Activity extends SafeGuideActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_guide1);
    }

    @Override
    public void next(View view) {
        startActivity(new Intent(this,SafeGuide2Activity.class));
        finish();

        super.next(view);
    }
}
