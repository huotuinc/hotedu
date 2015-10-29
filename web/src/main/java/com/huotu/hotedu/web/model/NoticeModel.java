package com.huotu.hotedu.web.model;

/**
 * Created by WenbinChen on 2015/10/28 20:34.
 */
public class NoticeModel {

    private long noticeId;

    private boolean enabled;

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
