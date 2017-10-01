package com.mobisquid.mobicash.dbstuff;


import android.content.Context;

import com.orm.SugarRecord;

public class MessageDb extends SugarRecord {
    private long timeRec;
    private String message;
    private String messageType;
    public String thread;
    public String extra;

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Boolean getIsself() {
        return isself;
    }

    public void setIsself(Boolean isself) {
        this.isself = isself;
    }

    public boolean isself;
    private String recep;
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    private String messageId;
    private String otherName;
    private String sender;
    private long timeSent;
    private String threadid;
    private String userId;
    private String delServ;
    private String recepNumber;
    private Long msgID;

    public Long getMsgID() {
        return msgID;
    }

    public void setMsgID(Long msgID) {
        this.msgID = msgID;
    }

    public String getRecepNumber() {
        return recepNumber;
    }

    public void setRecepNumber(String recepNumber) {
        this.recepNumber = recepNumber;
    }

    private String mobileNumber;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDelServ() {
        return delServ;
    }

    public void setDelServ(String delServ) {
        this.delServ = delServ;
    }

    public String getDelRecep() {
        return delRecep;
    }

    public void setDelRecep(String delRecep) {
        this.delRecep = delRecep;
    }

    private String delRecep;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getThreadid() {
        return threadid;
    }

    public void setThreadid(String threadid) {
        this.threadid = threadid;
    }

    public MessageDb() {

        // TODO Auto-generated constructor stub

    }

    public MessageDb(String threadID, long timerec, String message, String messagetype
            , String recep, String messageid, String otherName, String sender, long timesent, String userName, String userId,
                     String delServer, String delRecep, String mobilenumber, String recepNumber, String language,
                     boolean isself, String thread, String extra) {
        this.message = message;
        this.threadid = threadID;
        this.userName = userName;
        this.messageId = messageid;
        this.otherName = otherName;
        this.userId = userId;
        this.thread = thread;
        this.extra = extra;
        this.recep = recep;
        this.timeSent = timesent;
        this.sender = sender;
        this.timeRec = timerec;
        this.messageType = messagetype;
        this.delRecep = delRecep;
        this.delServ = delServer;
        this.mobileNumber = mobilenumber;
        this.recepNumber = recepNumber;
        this.isself = isself;
        this.language = language;


    }

    public long getTimeRec() {
        return timeRec;
    }

    public void setTimeRec(long timeRec) {
        this.timeRec = timeRec;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getRecep() {
        return recep;
    }

    public void setRecep(String recep) {
        this.recep = recep;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageid) {
        this.messageId = messageid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    @Override
    public String toString() {
        return message;

    }

}
