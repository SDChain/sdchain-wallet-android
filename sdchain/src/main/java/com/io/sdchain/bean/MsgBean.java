package com.io.sdchain.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by xiey on 2017/9/28.
 */

@Entity
public final class MsgBean implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * auto increase ID
     */
    @Id(autoincrement = true)
    private Long id;
    /**
     * user ID
     */
    private String userId;
    /**
     * read status
     */
    private boolean readStatus;

    private String hash;
    private String ledger;
    private String status;
    private String source_account;
    private String destination_account;
    private String currency;
    private String value;
    private String issuer;
    private String direction;
    private String timestamp;
    private String fee;
    private String memoData;
    private boolean success;
    private String account;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    @Generated(hash = 237905234)
    public MsgBean() {
    }

    @Generated(hash = 43683106)
    public MsgBean(Long id, String userId, boolean readStatus, String hash,
            String ledger, String status, String source_account,
            String destination_account, String currency, String value,
            String issuer, String direction, String timestamp, String fee,
            String memoData, boolean success, String account, String date) {
        this.id = id;
        this.userId = userId;
        this.readStatus = readStatus;
        this.hash = hash;
        this.ledger = ledger;
        this.status = status;
        this.source_account = source_account;
        this.destination_account = destination_account;
        this.currency = currency;
        this.value = value;
        this.issuer = issuer;
        this.direction = direction;
        this.timestamp = timestamp;
        this.fee = fee;
        this.memoData = memoData;
        this.success = success;
        this.account = account;
        this.date = date;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public String getSource_account() {
        return source_account;
    }

    public void setSource_account(String source_account) {
        this.source_account = source_account;
    }

    public String getDestination_account() {
        return destination_account;
    }

    public void setDestination_account(String destination_account) {
        this.destination_account = destination_account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getMemoData() {
        return memoData;
    }

    public void setMemoData(String memoData) {
        this.memoData = memoData;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getReadStatus() {
        return this.readStatus;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    @Override
    public String toString() {
        return "MsgBean{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", readStatus=" + readStatus +
                ", hash='" + hash + '\'' +
                ", ledger='" + ledger + '\'' +
                ", status='" + status + '\'' +
                ", source_account='" + source_account + '\'' +
                ", destination_account='" + destination_account + '\'' +
                ", currency='" + currency + '\'' +
                ", value='" + value + '\'' +
                ", issuer='" + issuer + '\'' +
                ", direction='" + direction + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", fee='" + fee + '\'' +
                ", memoData='" + memoData + '\'' +
                ", success=" + success +
                ", account='" + account + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
