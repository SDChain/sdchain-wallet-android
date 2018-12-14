package com.io.sdchain.eventbus;

/**
 * @author xiey
 * @date created at 2018/3/2 9:25
 * @package com.io.sdchain.eventbus
 * @project SDChain
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public class EventBusType {
    /**
     * type
     */
    BusType busType;

    /**
     * used object
     */
    Object obj;

    public EventBusType(BusType busType, Object obj) {
        this.busType = busType;
        this.obj = obj;
    }

    public BusType getBusType() {
        return busType;
    }

    public Object getObj() {
        return obj;
    }

    public enum BusType {
        //friend info
        FRIENDDETAL,
        //friend page -> pay page
        FRIENDDETAILTOPAY,
        //phone number register ok,finish register page
        REGISTBYPHONE,
        //email register ok,finish register page
        REGISTBYEMAIL,
        //after bind phone number,finish bind phone number page
        BINDPHONE,
        //after bind email,finish bind email page
        BINDEMAIL,
        //certification success,finish current page
        REALNAMECERTIFICATION,
        //after forget password,finish forget password page
        FORGETPWD,
        //index page ,scan->pay page
        ASSETSTOPAY,
        //index page ,scan->pay page
        ASSETSTOPAY2,
        //add or delete friend success
        ADDORDELETEFRIEND,
        //pay success,refresh index coin balance
        ASSETSCHANGEDS,
        //create wallet success,refresh wallet list
        CREATEACCOUNTSUCCESS,
        //after change wallet nick,refresh index wallet list
        UPDATEWALLETNAME,
        //after create wallet,index default wallet is create wallet
        CREATEWALLETSUCCESS,
        //read message success
        READMSGSUCCESS,
        //import  wallet success
        IMPORTWALLETSUCCESS,
        //buy->sell
        BUYTOSELL,
        //sell->buy
        SELLTOBUY,
        //get all token name
        ALLCODES,
        //credit
        CREDIT,
    }

}