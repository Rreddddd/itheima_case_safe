package lc.test.case_fase.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import lc.test.case_fase.R;
import lc.test.case_fase.util.ConstantValue;
import lc.test.case_fase.util.SpTools;

public class SplashActivity extends AppCompatActivity {

    private static final int CHECK_VERSION_SUCCESS=1;
    private static final int CHECK_VERSION_FAILURE=2;

    private SplashHandler splashHandler=new SplashHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        findViewById(R.id.rl_splash).startAnimation(animation);

        getVersionName();
        if(SpTools.getBoolean(this,ConstantValue.SETTING_UPDATE_STATE,false)){
            checkVersionCode();
        }else{
            enterHome();
        }
    }

    private void getVersionName(){
        try {
            ((TextView)findViewById(R.id.tv_version_name)).setText("版本号:"+getPackageManager().getPackageInfo(getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private long getVersionCode(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return getPackageManager().getPackageInfo(getPackageName(),0).getLongVersionCode();
            }else{
                return getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void checkVersionCode(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                long startMil=System.currentTimeMillis();
                InputStream inputStream=null;
                ByteArrayOutputStream outputStream=null;
                Message message = Message.obtain();
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://10.0.2.2:8080/version.json").openConnection();
                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);
                    if(urlConnection.getResponseCode()==200){
                        outputStream = new ByteArrayOutputStream();
                        inputStream = urlConnection.getInputStream();
                        byte[] buffer=new byte[1024];
                        int len;
                        while((len=inputStream.read(buffer,0,buffer.length))!=-1){
                            outputStream.write(buffer,0,len);
                        }
                        message.obj=new JSONObject(outputStream.toString());
                        message.what=CHECK_VERSION_SUCCESS;
                    }else{
                        message.what=CHECK_VERSION_FAILURE;
                    }
                    long endMil=System.currentTimeMillis();
                    long minus=4000-(endMil-startMil);
                    if(minus>0){
                        Thread.sleep(minus);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message.what=CHECK_VERSION_FAILURE;
                } finally {
                    if(outputStream!=null){
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(inputStream!=null){
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    splashHandler.handleMessage(message);
                }
                Looper.loop();
            }
        }.start();
    }

    private void enterHome(){
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    private void updateApp(final String url){
        RequestParams requestParams=new RequestParams(url);
        String path;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            path=Environment.getExternalStorageDirectory().getPath();
        }else{
            path=getCacheDir().getPath();
        }
        requestParams.setSaveFilePath(path+File.separator+"case_safe_2.apk");
        requestParams.setAutoRename(true);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.i("SplashActivity","下载完成");
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(result),"application/vnd.android.package-archive");
                SplashActivity.this.startActivityForResult(intent,0);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("SplashActivity",ex.getMessage());
                Log.i("SplashActivity","下载出错");
                enterHome();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                enterHome();
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                Log.i("SplashActivity","开始下载");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.i("SplashActivity","下载中");
            }
        });
    }

    private void showCheckDialog(String name,String des,final String url){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle(name);
        builder.setMessage(des);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateApp(url);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    private static class SplashHandler extends Handler{

        private WeakReference<SplashActivity> splashActivity;

        public SplashHandler(SplashActivity splashActivity){
            this.splashActivity=new WeakReference<SplashActivity>(splashActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case CHECK_VERSION_SUCCESS:
                    JSONObject jsonObject=(JSONObject)msg.obj;
                    try {
                        int code = jsonObject.getInt("code");
                        Log.i("SplashActivity","服务器code："+code);
                        Log.i("SplashActivity","本地ode："+splashActivity.get().getVersionCode());
                        if(code>splashActivity.get().getVersionCode()){
                            splashActivity.get().showCheckDialog(jsonObject.getString("name"),jsonObject.getString("des"),jsonObject.getString("url"));
                        }else{
                            splashActivity.get().enterHome();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case CHECK_VERSION_FAILURE:
                    Toast.makeText(splashActivity.get(),"服务器繁忙",Toast.LENGTH_SHORT).show();
                    splashActivity.get().enterHome();
                    break;
            }
        }
    }
}
