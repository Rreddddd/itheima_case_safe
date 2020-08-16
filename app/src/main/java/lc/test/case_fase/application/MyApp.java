package lc.test.case_fase.application;

import android.app.Application;

import org.xutils.x;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
