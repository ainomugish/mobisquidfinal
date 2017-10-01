/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobisquid.mobicash.model;



public class Transactions {

    private String type;
    private Integer transid;
    private String status;
    private String amount;
    private String transcode;
    private String details;
    private String timestamp;
    private String receiverMobile;
    private String senderMobile;
    private String senderName;
    private String receiverName;
    private String sendereMail;
    private String receivereMail;
    private String receiverToken;
    private String senderToken;
    private String extra;

    public Transactions() {
    }

    public Transactions(Integer transid) {
        this.transid = transid;
    }

  /*  public Transactions(Integer transid, String type, String status, String amount, String transcode, String details, Date timestamp, String receiverMobile, String senderMobile, String senderName, String receiverName, String sendereMail, String receivereMail, String receiverToken, String senderToken, String extra) {
        this.transid = transid;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.transcode = transcode;
        this.details = details;
        this.timestamp = timestamp;
        this.receiverMobile = receiverMobile;
        this.senderMobile = senderMobile;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.sendereMail = sendereMail;
        this.receivereMail = receivereMail;
        this.receiverToken = receiverToken;
        this.senderToken = senderToken;
        this.extra = extra;
    }*/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTransid() {
        return transid;
    }

    public void setTransid(Integer transid) {
        this.transid = transid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTranscode() {
        return transcode;
    }

    public void setTranscode(String transcode) {
        this.transcode = transcode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSendereMail() {
        return sendereMail;
    }

    public void setSendereMail(String sendereMail) {
        this.sendereMail = sendereMail;
    }

    public String getReceivereMail() {
        return receivereMail;
    }

    public void setReceivereMail(String receivereMail) {
        this.receivereMail = receivereMail;
    }

    public String getReceiverToken() {
        return receiverToken;
    }

    public void setReceiverToken(String receiverToken) {
        this.receiverToken = receiverToken;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transid != null ? transid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.transid == null && other.transid != null) || (this.transid != null && !this.transid.equals(other.transid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transactions[ transid=" + transid + " ]";
    }
    
}
