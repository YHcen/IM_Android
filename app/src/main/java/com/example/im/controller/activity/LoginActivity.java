package com.example.im.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.im.R;
import com.example.im.model.Model;
import com.example.im.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

//登录页面
public class LoginActivity extends Activity {
    private EditText et_login_name;
    private EditText et_login_pwd;
    private Button bt_login_regist;
    private Button bt_login_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);*/
        //初始化控件
        initView();

        //初始化监听
        initListener();
    }

    private void initListener() {
        //注册按钮的点击事件处理
        bt_login_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist();
            }
        });

        //登录按钮的点击事件处理
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    //登录按钮的逻辑处理
    private void login() {
        //1 获取输入的用户名和密码
        String loginName=et_login_name.getText().toString();
        String loginPwd = et_login_pwd.getText().toString();

        //2 校验输入的用户名和密码
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPwd)) {
            Toast.makeText(LoginActivity.this,"输入的用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        //具体的登录逻辑处理
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器登录
                EMClient.getInstance().login(loginName, loginPwd, new EMCallBack() {
                    //登录成功后的处理
                    @Override
                    public void onSuccess() {
                        //对模型层数据的处理
                        Model.getInstance().loginSuccess(new UserInfo(loginName));

                        //保存用户账号信息到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(loginName));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示登录成功
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                //跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                startActivity(intent);

                                finish();
                            }
                        });

                    }

                    //登录失败后的处理
                    @Override
                    public void onError(int i, String s) {
                        //提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录失败"+s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //登录过程中的处理
                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }
        });

    }

    //注册的业务逻辑处理
    private void regist() {
        //1 获取输入的用户名和密码
        String registName=et_login_name.getText().toString();
        String registPwd = et_login_pwd.getText().toString();

        //2 校验输入的用户名和密码
        if (TextUtils.isEmpty(registName) || TextUtils.isEmpty(registPwd)) {
            Toast.makeText(LoginActivity.this,"输入的用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        //3 去服务器注册
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //去环信服务器注册账号
                    EMClient.getInstance().createAccount(registName,registPwd);

                    //更新页面显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"注册失败"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        et_login_name=(EditText) findViewById(R.id.et_login_name);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        bt_login_regist = findViewById(R.id.bt_login_regist);
        bt_login_login = findViewById(R.id.bt_login_login);
    }
}
