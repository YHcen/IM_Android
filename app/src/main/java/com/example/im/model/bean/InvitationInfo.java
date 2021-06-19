package com.example.im.model.bean;

/**
 * @Author cyh
 * @Date 2021/6/3 15:11
 */
//邀请信息的bean类
public class InvitationInfo {
    private UserInfo user;
    private GroupInfo group;

    private String reason;

    private InvitationStatus status;

    public InvitationInfo() {
    }

    public InvitationInfo(UserInfo user, GroupInfo group, String reason, InvitationStatus status) {
        this.user = user;
        this.group = group;
        this.reason = reason;
        this.status = status;
    }

    public enum InvitationStatus {
        //联系人邀请信息状态
        NEW_INVITE,//新邀请
        INVITE_ACCEPT,//接受邀请
        INVITE_ACCEPT_BY_PEER,//邀请被接受
        INVITE_ACCEPT_BY_REJECT,//邀请被拒绝

        //群邀请状态

        NEW_GROUP_INVITE,

        NEW_GROUP_APPLICATION,

        GROUT_INVITE_ACCEPTED,

        GROUP_APPLICATION_ACCEPTED,

        //接受了群邀请
        GROUP_ACCEPT_INVITE,

        GROUP_ACCEPT_APPLICATION,

        GROUP_REJECT_INVITE,

        //拒绝了群申请加入

        GROUP_REJECT_APPLICATION,

        GROUT_INVITE_DECLINED,

        GROUP_APPLICATION_DECLINED
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public GroupInfo getGroup() {
        return group;
    }

    public void setGroup(GroupInfo group) {
        this.group = group;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InvitationInfo{" +
                "user=" + user +
                ", group=" + group +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }
}
