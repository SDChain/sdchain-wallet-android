package com.io.sdchain.bean;

import java.io.Serializable;

/**
 * @author xiey
 * @date created at 2018/4/18 9:26
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class InfoBean implements Serializable {

    private String title;
    private String readNum;
    private String newsId;
    private String content;
    private long timeDiff;
//    private String created_time;


    public long getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(long timeDiff) {
        this.timeDiff = timeDiff;
    }

//    public String getCreated_time() {
//        return created_time;
//    }
//
//    public void setCreated_time(String created_time) {
//        this.created_time = created_time;
//    }

    private String currency;
    private String limit;
    private String counterparty;
    private String pic;
    private boolean trusted;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

//    public String getCreated_time() {
//        return created_time;
//    }
//
//    public void setCreated_time(String created_time) {
//        this.created_time = created_time;
//    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    @Override
    public String toString() {
        return "CreditGrantBean{" +
                "currency='" + currency + '\'' +
                ", limit='" + limit + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", pic='" + pic + '\'' +
                ", trusted=" + trusted +
                '}';
    }
}
