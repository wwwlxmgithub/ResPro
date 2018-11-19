package cn.edu.xjtu.respro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * 基类Activity
 * Created by Mg on 2018/3/5.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    /**
     * 启动一个Activity，不带返回数据和结果
     * @param nextActivity 跳转的activity
     * @param fromActivity 本activity
     */
    protected void startActivity(Context fromActivity, Class nextActivity){
        Intent intent = new Intent();
        intent.setClass(fromActivity, nextActivity);
        startActivity(intent);
    }


    /**
     * 启动一个Activity，有返回结果
     * @param nextActivity 跳转的activity
     * @param fromActivity 本activity
     * @param reqCode 请求码
     */
    protected void startActivityForResult(Context fromActivity, Class nextActivity, int reqCode){
        Intent intent = new Intent();
        intent.setClass(fromActivity, nextActivity);
        startActivityForResult(intent, reqCode);
    }

    /**
     * 显示Toast
     * @param message 要显示的字符串
     */
    protected void showToastMessage(String message){
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast
     * @param resId 要显示的资源Id
     */
    protected void showToastMessage(int resId){
        Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_SHORT).show();
    }
}
