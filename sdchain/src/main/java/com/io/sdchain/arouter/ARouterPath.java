package com.io.sdchain.arouter;

/**
 * @author : xiey
 * @project name : SDChain.
 * @package name  : com.io.sdchain.arouter.
 * @date : 2018/7/19.
 * @signature : do my best.
 * @explain :
 */
public class ARouterPath {

    //login,register
    private static final String LOGINGROUP = "/LoginGroup";
    public static final String SplashActivity = LOGINGROUP + "/SplashActivity";
    public static final String LoginActivity = LOGINGROUP + "/LoginActivity";
    public static final String MainActivity = LOGINGROUP + "/MainActivity";
    public static final String ForgetPwdActivity = LOGINGROUP + "/ForgetPwdActivity";
    public static final String ForgetPwd2Activity = LOGINGROUP + "/ForgetPwd2Activity";
    public static final String RegisterPhoneActivity = LOGINGROUP + "/RegisterPhoneActivity";
    public static final String RegisterPhone2Activity = LOGINGROUP + "/RegisterPhone2Activity";
    public static final String WebViewActivity = LOGINGROUP + "/WebViewActivity";
    public static final String RegisterEmailActivity = LOGINGROUP + "/RegisterEmailActivity";
    public static final String RegisterEmail2Activity = LOGINGROUP + "/RegisterEmail2Activity";
    public static final String ForgetWalletPwdActivity = LOGINGROUP + "/ForgetWalletPwdActivity";


    //asset
    private static final String ASSETGROUP = "/AssetGroup";
    public static final String AssetFragment = ASSETGROUP + "/AssetFragment";
    public static final String AddWalletActivity = ASSETGROUP + "/AddWalletActivity";
    public static final String ImportWalletActivity = ASSETGROUP + "/ImportWalletActivity";
    public static final String CreateAccountActivity = ASSETGROUP + "/CreateAccountActivity";


    //pay
    private static final String TRANSFERGROUP = "/TransferGroup";
    public static final String PayFragment = TRANSFERGROUP + "/PayFragment";

    //mine
    private static final String MINEGROUP = "/MineGroup";
    public static final String MineFragment = MINEGROUP + "/MineFragment";
    public static final String WalletAddressActivity = MINEGROUP + "/WalletAddressActivity";
    public static final String FriendDetailActivity = MINEGROUP + "/FriendDetailActivity";
    public static final String BadCodeResultActivity = MINEGROUP + "/BadCodeResultActivity";
    public static final String BillActivity = MINEGROUP + "/BillActivity";
    public static final String FriendActivity = MINEGROUP + "/FriendActivity";
    public static final String BillDetailActivity = MINEGROUP + "/BillDetailActivity";
    public static final String MineActivity = MINEGROUP + "/MineActivity";
    public static final String SetWalletPwdActivity = MINEGROUP + "/SetWalletPwdActivity";
    public static final String CreditGrantActivity = MINEGROUP + "/CreditGrantActivity";
    public static final String MsgListActivity = MINEGROUP + "/MsgListActivity";
    public static final String SettingActivity = MINEGROUP + "/SettingActivity";
    public static final String AddFriendActivity = MINEGROUP + "/AddFriendActivity";
    public static final String BindEmailActivity = MINEGROUP + "/BindEmailActivity";
    public static final String BindEmailFirstActivity = MINEGROUP + "/BindEmailFirstActivity";
    public static final String BindPhoneActivity = MINEGROUP + "/BindPhoneActivity";
    public static final String BindPhoneFirstActivity = MINEGROUP + "/BindPhoneFirstActivity";
    public static final String CertificationActivity = MINEGROUP + "/CertificationActivity";
    public static final String CodeActivity = MINEGROUP + "/CodeActivity";
    public static final String CreditGrantRecordActivity = MINEGROUP + "/CreditGrantRecordActivity";
    public static final String KeyActivity = MINEGROUP + "/KeyActivity";
    public static final String MsgDetailActivity = MINEGROUP + "/MsgDetailActivity";
    public static final String SetLoginPwdActivity = MINEGROUP + "/SetLoginPwdActivity";


    //interceptor
    public static final String AgreementInterceptor = "AgreementInterceptor";
    public static final String SdkVersionInterceptor = "SdkVersionInterceptor";
    public static final String AboutUsInterceptor = "AboutUsInterceptor";

    public static final String FROM = "from";
    public static final String To = "to";
    public static final String DATA = "data";
    public static final String DATA1 = "data1";
    public static final String DATA2 = "data2";
    public static final String DATA3 = "data3";
    public static final String DATA4 = "data4";
    public static final String DATA5 = "data5";
    public static final String DATA6 = "data6";

    //priority
    public static final int PRIORITY_1=1;
    public static final int PRIORITY_2=2;
    public static final int PRIORITY_3=3;
    public static final int PRIORITY_4=4;
    public static final int PRIORITY_5=5;
    public static final int PRIORITY_6=6;
    public static final int PRIORITY_7=7;
    public static final int PRIORITY_8=8;
    public static final int PRIORITY_9=9;
    public static final int PRIORITY_10=10;


}
