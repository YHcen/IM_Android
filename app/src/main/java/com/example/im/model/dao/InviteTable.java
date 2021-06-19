package com.example.im.model.dao;

/**
 * @Author cyh
 * @Date 2021/6/3 16:40
 */
//邀请信息表
public class InviteTable {

    public static final String TAB_NAME = "tab_invite";
    public static final String COL_USER_HXID = "user_hxid";//用户的环信id
    public static final String COL_USER_NAME = "user_name";//用户昵称
    public static final String COL_GROUP_NAME = "group_name";//群组名称
    public static final String COL_GROUP_HXID = "group_hxid";//群组环信id
    public static final String COL_REASON = "reason";//邀请的原因
    public static final String COL_STATUS = "status";//邀请的状态

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_USER_HXID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_GROUP_NAME + " text,"
            + COL_GROUP_HXID + " text,"
            + COL_REASON + " text,"
            + COL_STATUS + " integer);";

}
