package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiey
 * @date created at 2018/5/28 18:14
 * @package com.io.sdchain.bean
 * @project SDChain
 */

public final class BillDetailBean implements Serializable{

    private String hash;
    private String ledger;
    private String source_account;
    private String destination_account;
    private AmountBean amount;
    private String direction;
    private String timestamp;
    private String fee;
    private List<MemosBean> memos;
    private String state;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public AmountBean getAmount() {
        return amount;
    }

    public void setAmount(AmountBean amount) {
        this.amount = amount;
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

    public List<MemosBean> getMemos() {
        return memos;
    }

    public void setMemos(List<MemosBean> memos) {
        this.memos = memos;
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

    public static class MemosBean implements Serializable {
        /**
         * memo_type : 6D656D6F
         * memo_data : E6B58BE8AF95
         */

        private String memo_type;
        private String memo_data;
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

        public String getMemo_type() {
            return memo_type;
        }

        public void setMemo_type(String memo_type) {
            this.memo_type = memo_type;
        }

        public String getMemo_data() {
            return memo_data;
        }

        public void setMemo_data(String memo_data) {
            this.memo_data = memo_data;
        }

        @Override
        public String toString() {
            return "MemosBean{" +
                    "memo_type='" + memo_type + '\'' +
                    ", memo_data='" + memo_data + '\'' +
                    ", MemoData='" + MemoData + '\'' +
                    ", MemoType='" + MemoType + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BillDetailBean{" +
                "hash='" + hash + '\'' +
                ", ledger='" + ledger + '\'' +
                ", source_account='" + source_account + '\'' +
                ", destination_account='" + destination_account + '\'' +
                ", amount=" + amount +
                ", direction='" + direction + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", fee='" + fee + '\'' +
                ", memos=" + memos +
                ", state='" + state + '\'' +
                '}';
    }
}
