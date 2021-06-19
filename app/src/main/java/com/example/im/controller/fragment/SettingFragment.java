package com.example.im.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.im.R;
import com.example.im.controller.activity.LoginActivity;
import com.example.im.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseTitleBar;

/**
 * @Author cyh
 * @Date 2021/6/1 19:22
 */
public class SettingFragment extends Fragment {
    private Button bt_setting_logout;
    private TextView userId_view;
    private TextView nickName_view;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_about_me, null);

        initView(view);
        return view;
    }

    private void initView(View view) {

        nickName_view = view.findViewById(R.id.tv_nickName);
        userId_view = view.findViewById(R.id.tv_userId);
        bt_setting_logout = view.findViewById(R.id.bt_setting_logout);

    }


    @Override
    public void onActivityCreated(@Nullable  Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        //在button上显示当前用户名称
        //bt_setting_logout.setText("退出登录("+ EMClient.getInstance().getCurrentUser()+")");
        //在TextView上显示当前用户名称
        nickName_view.setText("账号：" + EMClient.getInstance().getCurrentUser());

        //退出登录的逻辑处理
        bt_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器退出登录
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {

                                //关闭DBHelper
                                Model.getInstance().getDbManager().close();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //更新ui显示
                                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();

                                        //回到登录页面
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);

                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int code, String error) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "退出失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });
                    }
                });
            }
        });
    }
}
