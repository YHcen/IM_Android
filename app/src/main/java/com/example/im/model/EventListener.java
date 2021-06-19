package com.example.im.model;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.im.model.bean.InvitationInfo;
import com.example.im.model.bean.UserInfo;
import com.example.im.utils.Constant;
import com.example.im.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

/**
 * @Author cyh
 * @Date 2021/6/4 11:20
 */
//全局事件监听类
public class EventListener {

    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context) {
        mContext = context;

        //创建一个发送广播的管理者对象
        mLBM = LocalBroadcastManager.getInstance(mContext);

        //注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
    }

    private final EMContactListener emContactListener =new EMContactListener() {
        //联系人增加后执行的方法
        @Override
        public void onContactAdded(String hxid) {
            //数据更新
            Model.getInstance().getDbManager().getContactTableDao().saveContact(new UserInfo((hxid)), true);

            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxid) {
            //数据更新
            Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(hxid);
            Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(hxid);

            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //接收到联系人的新邀请
        @Override
        public void onContactInvited(String hxid, String reason) {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);//新的邀请

            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人同意了邀请
        @Override
        public void onFriendRequestAccepted(String hxid) {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);//别人同意了你的邀请

            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人拒绝了你的好友邀请
        @Override
        public void onFriendRequestDeclined(String username) {

            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
