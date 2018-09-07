package com.io.sdchain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiey on 2017/9/27.
 */

public final class BillBean implements Serializable {

    private AmountBean amount;
    private String delivered_amount;
    private int tx_index;
    private String currency;
    private String destination;
    private String executed_time;
    private String issuer;
    private int ledger_index;
    private String source;
    private String source_currency;
    private String tx_hash;
    private String transaction_cost;
    private String timestamp;
    private String source_account;
    private String destination_account;
    private String hash;
    private String direction;
    private List<DestinationBalanceChangesBean> destination_balance_changes;
    private List<SourceBalanceChangesBean> source_balance_changes;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AmountBean getAmount() {
        return amount;
    }

    public void setAmount(AmountBean amount) {
        this.amount = amount;
    }

    public String getDelivered_amount() {
        return delivered_amount;
    }

    public void setDelivered_amount(String delivered_amount) {
        this.delivered_amount = delivered_amount;
    }

    public int getTx_index() {
        return tx_index;
    }

    public void setTx_index(int tx_index) {
        this.tx_index = tx_index;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getExecuted_time() {
        return executed_time;
    }

    public void setExecuted_time(String executed_time) {
        this.executed_time = executed_time;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public int getLedger_index() {
        return ledger_index;
    }

    public void setLedger_index(int ledger_index) {
        this.ledger_index = ledger_index;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_currency() {
        return source_currency;
    }

    public void setSource_currency(String source_currency) {
        this.source_currency = source_currency;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public void setTx_hash(String tx_hash) {
        this.tx_hash = tx_hash;
    }

    public String getTransaction_cost() {
        return transaction_cost;
    }

    public void setTransaction_cost(String transaction_cost) {
        this.transaction_cost = transaction_cost;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<DestinationBalanceChangesBean> getDestination_balance_changes() {
        return destination_balance_changes;
    }

    public void setDestination_balance_changes(List<DestinationBalanceChangesBean> destination_balance_changes) {
        this.destination_balance_changes = destination_balance_changes;
    }

    public List<SourceBalanceChangesBean> getSource_balance_changes() {
        return source_balance_changes;
    }

    public void setSource_balance_changes(List<SourceBalanceChangesBean> source_balance_changes) {
        this.source_balance_changes = source_balance_changes;
    }

    public static class AmountBean implements Serializable{
        /**
         * value : 5
         * currency : CNY
         * issuer : 6UPd52jHtu1d88nc3S3WeroACFQpKfybhU
         */

        private String value;
        private String currency;
        private String issuer;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }
    }

    public static class DestinationBalanceChangesBean implements Serializable{
        /**
         * counterparty : 6UPd52jHtu1d88nc3S3WeroACFQpKfybhU
         * currency : CNY
         * value : 5
         */

        private String counterparty;
        private String currency;
        private String value;

        public String getCounterparty() {
            return counterparty;
        }

        public void setCounterparty(String counterparty) {
            this.counterparty = counterparty;
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
    }

    public static class SourceBalanceChangesBean implements Serializable{
        /**
         * counterparty : 6UPd52jHtu1d88nc3S3WeroACFQpKfybhU
         * currency : CNY
         * value : -5
         */

        private String counterparty;
        private String currency;
        private String value;

        public String getCounterparty() {
            return counterparty;
        }

        public void setCounterparty(String counterparty) {
            this.counterparty = counterparty;
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
    }
}
