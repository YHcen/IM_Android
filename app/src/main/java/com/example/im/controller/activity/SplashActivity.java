package com.example.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;

import com.example.im.R;
import com.example.im.model.Model;
import com.example.im.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

//欢迎页面
public class SplashActivity extends Activity {

    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            //如果当前activity已经退出，那么就不处理handler中的消息
            if(isFinishing()){
                return;
            }
            //判断进入主页面还是登录页面
            toMainOrLogin();
        }
        //判断进入主页面还是登录页面
        private void toMainOrLogin() {
/*            new Thread(){
                public void run() {

                }
            }.start();*/

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    //判断当前账号是否已经登录过
                    if (EMClient.getInstance().isLoggedInBefore()) {//登录过

                        //获取当前登录用户的信息
                        UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxid(EMClient.getInstance().getCurrentUser());

                        if(account==null){
                            //跳转到登录页面
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            //登录成功后的方法
                            Model.getInstance().loginSuccess(account);

                            //跳转到主页面
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } else {//没登录过

                        //跳转到登录页面
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    //结束当前页面
                    finish();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acvtivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //设置顶部状态栏为透明
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);


        //发送2S的延时消息
        handler.sendMessageDelayed(Message.obtain(), 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁消息
        handler.removeCallbacksAndMessages(null);
    }
}
