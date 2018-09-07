package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiey on 2017/9/28.
 */

public final class MsgNetBean implements Serializable {

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
    private String state;
    private String source_account;
    private String destination_account;
    private AmountBean amount;
    private String direction;
    private String timestamp;
    private String fee;
    private List<MemosBean> memos;
    private boolean success;
    private String account;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId==null?"":userId;
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
        return hash==null?"":hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getLedger() {
        return ledger==null?"":ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public String getSource_account() {
        return source_account==null?"":source_account;
    }

    public void setSource_account(String source_account) {
        this.source_account = source_account;
    }

    public String getDestination_account() {
        return destination_account==null?"":destination_account;
    }

    public void setDestination_account(String destination_account) {
        this.destination_account = destination_account;
    }

    public AmountBean getAmount() {
        return amount;
    }

    public void setAmount(AmountBean amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction==null?"":direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTimestamp() {
        return timestamp==null?"":timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFee() {
        return fee==null?"":fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public List<MemosBean> getMemos() {
        return memos;
    }

    public void setMemos(List<MemosBean> memos) {
        this.memos = memos;
    }

    public static class MemosBean implements Serializable {
        /**
         * MemoData : E591B5E591B5E591B5
         * MemoType : 6D656D6F
         */

        private String MemoData;
        private String MemoType;

        public String getMemoData() {
            return MemoData;
        }

        public void setMemoData(String MemoData) {
            this.MemoData = MemoData;
        }

        public String getMemoType() {
            return MemoType;
        }

        public void setMemoType(String MemoType) {
            this.MemoType = MemoType;
        }

        @Override
        public String toString() {
            return "MemosBean{" +
                    "MemoData='" + MemoData + '\'' +
                    ", MemoType='" + MemoType + '\'' +
                    '}';
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getState() {
        return state==null?"":state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccount() {
        return account==null?"":account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public static class AmountBean implements Serializable {
        /**
         * currency : SDA
         * value : 7
         * issuer :
         */

        private String currency;
        private String value;
        private String issuer;

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

        @Override
        public String toString() {
            return "AmountBean{" +
                    "currency='" + currency + '\'' +
                    ", value='" + value + '\'' +
                    ", issuer='" + issuer + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "MsgNetBean{" +
                "userId='" + userId + '\'' +
                ", readStatus=" + readStatus +
                ", hash='" + hash + '\'' +
                ", ledger='" + ledger + '\'' +
                ", state='" + state + '\'' +
                ", source_account='" + source_account + '\'' +
                ", destination_account='" + destination_account + '\'' +
                ", amount='" + amount + '\'' +
                ", direction='" + direction + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", fee='" + fee + '\'' +
                ", memos=" + memos +
                ", success=" + success +
                ", account='" + account + '\'' +
                '}';
    }
}
