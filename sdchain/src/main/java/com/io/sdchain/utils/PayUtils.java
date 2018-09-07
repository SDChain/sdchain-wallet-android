package com.io.sdchain.utils;

import android.content.Context;
import android.text.TextUtils;

import com.io.sdchain.bean.BalanceBean;
import com.io.sdchain.bean.MoneyBean;
import com.io.sdchain.bean.UserBean;
import com.io.sdchain.common.Constants;
import com.io.sdchain.common.ErrorCode;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author : xiey
 * @project name : sdchian.
 * @package name  : com.io.sdchian.utils.
 * @date : 2018/6/28.
 * @signature : do my best.
 * @explain :
 */
public class PayUtils {
    /**
     * Detection of real name certification
     * Is the current account your own account?
     * Get the total amount, frozen amount, and available amount of the current account
     * Get the entered amount and check the legality of the input amount, limit 6 digits
     *  * Determine whether the amount entered and the available amount are sufficient
     */
    private static final String ZERO = "0";
    private static final char ZERO_CHAR = '0';
    private static final String POINT = ".";
    private static final char POINT_CHAR = '.';
    private static final String CURRENCY = "SDA";
    private static final String RAX = ".1234567890";
    private static final String ZERO_POINT_ZERO = "0.0";

    public static int checkPay(Context context, String payAddress, String currency, String inputMoney) {
        /****0.Preparation parameters****/
        SaveObjectTools tools = new SaveObjectTools(context);
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        /****1.Check for real name authentication****/
//        if (!userBean.getCertStatus().equals("1")) {
//            return ErrorCode.ERROE1;
//        }
        /****2.Whether the current account is your own account****/
        if (payAddress.equals(userBean.getAccount())) {
            return ErrorCode.ERROE2;
        }
        /****3.Gets the total amount, frozen amount, available amount of the current account****/
        MoneyBean moneyBean = balance(context, currency);
        /****4.Get the input amount and check the validity of the input amount****/
        if (!checkInputMoney(inputMoney)) {
            return ErrorCode.ERROE3;
        }
        //Cannot be zero
        String inputValueTemp = getMoney(inputMoney);
        if (inputValueTemp.equals(ZERO)) {
            return ErrorCode.ERROE6;
        }
        /****5.The input amount is limited to 6 digits****/
        if (!limitSix(inputValueTemp)) {
            return ErrorCode.ERROE4;
        }
        /****6.Determine whether the input and available amounts are sufficient for comparison****/
        BigDecimal inputValue = new BigDecimal(inputValueTemp);
        if (currency.equals(CURRENCY) && moneyBean.getCanUse().compareTo(inputValue) == -1) {
            return ErrorCode.ERROE5;
        } else if (moneyBean.getCanUse().compareTo(inputValue) == -1) {
            return ErrorCode.ERROE5;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 1. Limit input, there is no negative number, so it can be limited to [ . 1 2 3 4 5 6 7 8 9 0 ]
     * 1.1, input format detection: multiple decimal points, error; decimal point at the beginning, error; decimal point end, error;
     * 2, if it starts with 0
     * 2.0, first check if there is a decimal point to distinguish between integers and decimals
     * 2.1, the integer part: if all is 0, it is equal to 0; if there is a number other than 0, then remove the previous 0, return the integer part;
     * 2.2, fractional part: take the decimal point as the dividing line, test and judge
     * 2.2.1, the left of the decimal point: if both are 0, the left is 0; if there is a number other than 0, then the previous 0 is removed, and the integer part is retained;
     * 2.2.2, the decimal point to the right: if it is 0, it is 0; if there is a number other than 0, the following 0 is removed;
     * 2.3, if the final formatted number is 0.0, it is 0;
     * 3. If it is not 0, [1000, 10.12, 10.0, 10.000]
     * 3.0, first check if there is a decimal point to distinguish between integers and decimals
     * 3.1, integer part: no problem, no need to detect
     * 3.2, fractional part: take the decimal point as the dividing line, test and judge
     * 3.2.1, the decimal point to the left: no problem, no need to detect
     * 3.2.2, the right of the decimal point: if it is 0, it is 0; if there is a number other than 0, the following 0 is removed;
     * 3.3, if the final formatted fractional part is 0, then only integers
     *
     * @param inputMoney
     * @return
     */
    private static boolean checkInputMoney(String inputMoney) {
        if (TextUtils.isEmpty(inputMoney)) {
            return false;
        }
        char[] temp = inputMoney.toCharArray();
        //1、Limit the input, there is no negative number, so you can limit it to [.1, 2, 3, 4, 5, 6, 7, 8, 9, 0]
        if (!limitInput(inputMoney, temp)) {
            return false;
        }
        //1.1、Input format detection: multiple decimal points, error; The decimal point starts with an error; The decimal point ends, error;
        if (!checkFormat(temp)) {
            return false;
        }

        //There's only one decimal point
        if (inputMoney.equals(POINT)) {
            return false;
        }

        return true;
    }

    public static String getMoney(String inputMoney) {
        //The decimal point begins, fault tolerance; The decimal point ends, fault tolerance;
        inputMoney = startPoint(inputMoney) ? ZERO + inputMoney : inputMoney;
        inputMoney = endPoint(inputMoney) ? inputMoney + ZERO : inputMoney;

        Logger.i("inputMoney：" + inputMoney);

        //2、If it starts with 0
        if (inputMoney.startsWith(ZERO)) {
            //2.0、First check for a decimal point to distinguish between integers and decimals
            if (!inputMoney.contains(POINT)) {
                //2.1、Integral part: if all is 0, is equal to 0; If there is a number other than 0, remove the previous 0 and return the integer part;
                inputMoney = allZero(inputMoney) ? ZERO : deleteZeroBefore(inputMoney);
                return inputMoney;
            } else {
                //2.2、The decimal part: take the decimal point as the dividing line to detect and judge
                String left = inputMoney.substring(0, inputMoney.indexOf(POINT_CHAR));
                String right = inputMoney.substring(inputMoney.indexOf(POINT_CHAR) + 1, inputMoney.length());
                Logger.i("have 0:   left:" + left + "   right:" + right);
                //2.2.1、To the left of the decimal point: if both are 0, the left is 0; If there are Numbers other than 0, remove the previous 0 and keep the integer part.
                //2.2.2、To the right of the decimal point: if it's all 0, it's 0; If you have a number other than 0, get rid of the 0;
                left = allZero(left) ? ZERO : deleteZeroBefore(left);
                right = allZero(right) ? ZERO : deleteZeroAfter(right);
                String tempNum = right.equals(ZERO) ? left : left + POINT + right;
                return tempNum.equals(ZERO_POINT_ZERO) ? ZERO : tempNum;
            }
        } else {
            // 3、If it doesn't start with 0
            // 3.0、First check for a decimal point to distinguish between integers and decimals
            // 3.1、Integral part: no problem, no need to check
            if (inputMoney.contains(POINT)) {
                //3.2、The decimal part: take the decimal point as the dividing line to detect and judge
                String left = inputMoney.substring(0, inputMoney.indexOf(POINT_CHAR));
                String right = inputMoney.substring(inputMoney.indexOf(POINT_CHAR) + 1, inputMoney.length());
                Logger.i("don't have 0:   left:" + left + "   right:" + right);
                //3.2.1、To the left of the decimal point: no problem
                //3.2.2、To the right of the decimal point: if it's all 0, it's 0; If you have a number other than 0, get rid of the latter 0;
                //3.3、If the final formatting decimal is 0, only the integer is taken
                right = allZero(right) ? ZERO : deleteZeroAfter(right);
                String tempNum = right.equals(ZERO) ? left : left + POINT + right;
                return right.equals(ZERO) ? left : tempNum;
            }
            return inputMoney;
        }
    }

    /**
     * Limit the input, there is no negative number, so you can limit it to [.1, 2, 3, 4, 5, 6, 7, 8, 9, 0]
     *
     * @param num
     * @return
     */
    private static boolean limitInput(String num, char[] temp) {
        for (char c : temp) {
            if (RAX.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Format to check
     * Input format detection: multiple decimal points, error; The decimal point starts with an error; The decimal point ends, error;
     *
     * @param temp
     * @return
     */
    private static boolean checkFormat(char[] temp) {
        int idotIndex = 0;
        for (char c : temp) {
            if (c == POINT_CHAR) {
                ++idotIndex;
            }
        }
        if (idotIndex >= 2) {
            return false;
        }

        return true;
    }

    /**
     * 0 at the beginning
     *
     * @param num
     * @return
     */
    private static boolean startPoint(String num) {
        return num.startsWith(POINT) ? true : false;
    }

    /**
     * 0 at the end
     *
     * @param num
     * @return
     */
    private static boolean endPoint(String num) {
        return num.endsWith(POINT) ? true : false;
    }

    /**
     * Whether all of them are 0's
     *
     * @param num
     * @return
     */
    private static boolean allZero(String num) {
        char[] temp = num.toCharArray();
        for (char c : temp) {
            if (c != ZERO_CHAR) {
                return false;
            }
        }
        return true;
    }

    /**
     * Delete the previous 0
     *
     * @param num
     * @return
     */
    private static String deleteZeroBefore(String num) {
        if (allZero(num)) {
            return ZERO;
        }
        while (num.startsWith(ZERO)) {
            num = num.substring(1, num.length());
        }
        return num;
    }

    /**
     * I'm going to delete that 0
     *
     * @param num
     * @return
     */
    private static String deleteZeroAfter(String num) {
        if (allZero(num)) {
            return ZERO;
        }
        while (num.endsWith(ZERO)) {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }

    /**
     * Decimal limit 6 digits
     */
    private static boolean limitSix(String inputMoney) {
        if (inputMoney.contains(POINT)) {
            String right = inputMoney.substring(inputMoney.indexOf(POINT_CHAR) + 1, inputMoney.length());
            if (right.length() > 6) {
                return false;
            }
        }
        return true;
    }

    public static MoneyBean balance(Context context, String currency) {
        SaveObjectTools tools = new SaveObjectTools(context);
        UserBean userBean = (UserBean) tools.getObjectData(Constants.USERINFO);
        ArrayList<BalanceBean> balanceBeens = (ArrayList<BalanceBean>) tools.getObjectData(userBean.getUserAccountId());
        String freezeEct = (String) tools.getObjectData(Constants.FREEZESDA);
        //Empty processing
        if (TextUtils.isEmpty(currency)) {
            currency = CURRENCY;
        }
        //Gets all balances - matches based on the currently selected currency and currency list, returns the correct amount if any, and returns 0 if none
        String moneyValue = ListUtils.getValue(currency, balanceBeens).replace(",", "");
        //Get all balances
        BigDecimal moneyHasBig = new BigDecimal(moneyValue);
        //Empty processing of frozen amount, default is 0.6
        if (TextUtils.isEmpty(freezeEct)) {
            freezeEct = Constants.FREEEZENUM;
        }
        //SDA freeze baseline, exceeding less than or equal to 0.6 do not operate
        BigDecimal baseLine = new BigDecimal(freezeEct);
        //Freezing reference line for other currencies is 0
        BigDecimal baseOther = new BigDecimal(0);
        //
        BigDecimal freeLine = new BigDecimal(0);
        //SDA: frozen amount, available amount and frozen base line 0.6 are compared. If less than 0.6, the specific amount already frozen
        BigDecimal moneyFreezeBig = moneyHasBig.compareTo(baseLine) == -1 ? moneyHasBig : baseLine;
        //Freeze amount, other currencies and 0 for comparison
        BigDecimal moneyFreezeBigOther = moneyHasBig.compareTo(baseOther) == -1 ? moneyHasBig : baseOther;
        //SDA: available, the total amount is compared with the zero baseline after removing the frozen amount. If it is greater than 0, the available balance is the total amount - frozen amount, otherwise 0
        BigDecimal moneyUse = moneyHasBig.subtract(moneyFreezeBig).compareTo(freeLine) == 1 ? moneyHasBig.subtract(moneyFreezeBig) : freeLine;
        //Where other currencies are available, after deducting the frozen amount and comparing with the zero reference line, if greater than 0, the total amount shall be taken - frozen amount, otherwise 0
        BigDecimal moneyUseOther = moneyHasBig.subtract(moneyFreezeBigOther).compareTo(freeLine) == 1 ? moneyHasBig.subtract(moneyFreezeBigOther) : freeLine;
        MoneyBean moneyBean = new MoneyBean();
        if (currency.equals(CURRENCY)) {
            moneyBean.setCanUse(moneyUse.setScale(6, BigDecimal.ROUND_HALF_UP));
            moneyBean.setFreeze(moneyFreezeBig.setScale(6, BigDecimal.ROUND_HALF_UP));
            moneyBean.setAll(moneyHasBig);
        } else {
            moneyBean.setCanUse(moneyUseOther.setScale(6, BigDecimal.ROUND_HALF_UP));
            moneyBean.setFreeze(moneyFreezeBigOther.setScale(6, BigDecimal.ROUND_HALF_UP));
            moneyBean.setAll(moneyHasBig);
        }
        return moneyBean;
    }

}
