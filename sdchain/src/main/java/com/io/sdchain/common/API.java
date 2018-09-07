package com.io.sdchain.common;


import static com.io.sdchain.BuildConfig.BASE_URL;

public class API {
    public static String BASEURL = BASE_URL;

    /**
     * login
     */
    public static final String LOGIN = BASEURL + "resSDnWalt/user/login";

    /**
     * get phone code
     */
    public static final String PHONECODE = BASEURL + "resSDnWalt/sms/getPhoneCode";

    /**
     * get email code
     */
    public static final String EMAILCODE = BASEURL + "resSDnWalt/sms/getEmailCode";

    /**
     * get image code
     */
    public static final String IMAGECODE = BASEURL + "resSDnWalt/sms/getRandomCodePic";

    /**
     * pass phone verify
     */
    public static final String REGISTERBYPHONE = BASEURL + "resSDnWalt/user/registByPhone";

    /**
     * pass email  verify
     */
    public static final String REGISTERBYEMAIL = BASEURL + "resSDnWalt/user/registByEmail";

    /**
     * register
     */
    public static final String REGISTER = BASEURL + "resSDnWalt/user/regist";

    /**
     * certification
     */
    public static final String REALNAME = BASEURL + "resSDnWalt/user/realName";

    /**
     * forget password
     */
    public static final String FORGETPASSWORD = BASEURL + "resSDnWalt/user/forgetPassword";

    /**
     * change nick
     */
    public static final String UPDATENICKNAME = BASEURL + "resSDnWalt/user/updateNickname";

    /**
     * bind email
     */
    public static final String BINDEMAIL = BASEURL + "resSDnWalt/user/bindEmail";

    /**
     * bind phone number
     */
    public static final String BINDPHONE = BASEURL + "resSDnWalt/user/bindPhone";

    /**
     * get wallet address
     *
     * @deprecated
     */
    public static final String GETWALLETACCOUNT = BASEURL + "resSDnWalt/user/getWalletAccount";

    /**
     * get wallet key
     */
    public static final String GETWALLETSECRET = BASEURL + "resSDnWalt/user/getWalletSecret";

    /**
     * get friends list
     *
     * @deprecated
     */
    public static final String GETFRIENDLIST = BASEURL + "resSDnWalt/user/getFriendList";

    /**
     * get friend info
     */
    public static final String GETFRIENDINFO = BASEURL + "resSDnWalt/user/getFriendInfo";

    /**
     * add friend
     */
    public static final String ADDFRIEND = BASEURL + "resSDnWalt/user/addFriend";

    /**
     * search user
     */
    public static final String SEARCHUSER = BASEURL + "resSDnWalt/user/searchUser";

    /**
     * delete friend
     */
    public static final String DELETEFRIEND = BASEURL + "resSDnWalt/user/deleteFriend";

    /**
     * blurry search friend
     */
    public static final String SEARCHFRIEND = BASEURL + "resSDnWalt/user/searchFriend";

    /**
     * change password
     */
    public static final String UPDATEPASSWORD = BASEURL + "resSDnWalt/user/updatePassword";

    /**
     * change pay password
     */
    public static final String UPDATEPAYPASSWORD = BASEURL + "resSDnWalt/user/updatePayPassword";

    /**
     * get message list
     */
    public static final String GETMESSAGELIST = BASEURL + "resSDnWalt/user/getMessageList";

    /**
     * git message detail
     */
    public static final String GETMESSAGEINFO = BASEURL + "resSDnWalt/user/getMessageInfo";

    /**
     * active wallet
     */
    public static final String ACTIVATION = BASEURL + "resSDnWalt/payment/activation";

    /**
     * change wallet nick
     */
    public static final String UPDATEACCOUNTNAME = BASEURL + "resSDnWalt/user/updateAccountname";

    /**
     * forget wallet password
     */
    public static final String FORGETWALLETPASSWORD = BASEURL + "resSDnWalt/user/forgetWalletPassword";

    /**
     * change wallet password
     */
    public static final String UPDATEWALLETPASSWORD = BASEURL + "resSDnWalt/user/updateWalletPassword";

    /**
     * create wallet
     */
    public static final String CREATEWALLET = BASEURL + "resSDnWalt/payment/createWallet";

    /**
     * transfer
     */
    public static final String ISSUECURRENCY = BASEURL + "resSDnWalt/payment/issueCurrency";

    /**
     * get asset
     */
    public static final String GETBALANCE = BASEURL + "resSDnWalt/payment/getBalance";

    /**
     * get trade list
     */
    public static final String GETPAYMENTSLIST = BASEURL + "resSDnWalt/payment/getPaymentsList";

    /**
     * git trade detail
     */
    public static final String GETPAYMENTSINFO = BASEURL + "resSDnWalt/payment/getPaymentsInfo";

    /**
     * change default wallet
     */
    public static final String CHANGEDEFAULTWALLET = BASEURL + "resSDnWalt/payment/changeDefaultWallet";

    /**
     * index page get wallet list
     */
    public static final String WALLETLIST = BASEURL + "resSDnWalt/payment/walletList";

    /**
     * version update
     */
    public static final String CHECKVERSION = BASEURL + "resSDnWalt/version/checkVersion";

    /**
     * service agreement
     */
    public static final String SERVICEAGREEMENT_EN = BASEURL + "serviceAgreement_en.html";
    public static final String SERVICEAGREEMENT_TC = BASEURL + "serviceAgreement_tc.html";
    public static final String SERVICEAGREEMENT = BASEURL + "serviceAgreement.html";

    /**
     * about us
     */
    public static final String ABOUTUS = BASEURL + "about.html";
    public static final String ABOUTUS_EN = BASEURL + "about_en.html";
    public static final String ABOUTUS_TC = BASEURL + "about_tc.html";

    /**
     * git trust list
     * @deprecated
     */
    public static final String GETTRUSTLINES = BASEURL + "resSDnWalt/payment/getTrustlines";

    /**
     * git trust history
     */
    public static final String GETHISTRUSTLINES = BASEURL + "resSDnWalt/payment/getHisTrustlines";

    /**
     * import wallet
     */
    public static final String IMPORTWALLET = BASEURL + "resSDnWalt/payment/importWallet";

}
