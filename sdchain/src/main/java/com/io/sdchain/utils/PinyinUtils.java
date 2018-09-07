package com.io.sdchain.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * Created by xiey on 2017/10/10.
 */

public class PinyinUtils {
    /**
     * If the first character of a string is Chinese, returns the first letter of the Chinese character's phonetic uppercase
     * If the first character of a string is a letter, it is returned as a capital letter
     * Otherwise, return ‘ ’
     */
    public static char getHeadChar(String str) {
        //Safety judgment
        if (str != null && str.trim().length() != 0) {
            //Gets the first character
            char[] strChar = str.toCharArray();
            char headChar = strChar[0];
            //Returns a capital letter
            if (Character.isUpperCase(headChar)) {
                return headChar;
            } else if (Character.isLowerCase(headChar)) {
                return Character.toUpperCase(headChar);
            }
            //Chinese pinyin format output class
            HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
            hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            //\u4E00-\u9FA5]+Is the coding range of all Chinese characters
            if (String.valueOf(headChar).matches("[\\u4E00-\\u9FA5]+")) {
                String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(headChar);
                if (stringArray != null && stringArray[0] != null) {
                    return Character.toUpperCase(stringArray[0].charAt(0));
                }
            }
        }
        return ' ';
    }

    /**
     * Turn Chinese characters into pinyin
     *
     * @param hanzi Chinese characters or letters
     * @return spell
     */
    public static String getPinyin(String hanzi) {
        StringBuilder sb = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //Since you can't directly convert more than one man, you can only convert a single man
        char[] arr = hanzi.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (Character.isWhitespace(arr[i])) {
                continue;
            }
            try {
                String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(arr[i], format);
                if (pinyinArr != null) {
                    sb.append(pinyinArr[0]);
                } else {
                    sb.append(arr[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Not the correct Chinese characters
                sb.append(arr[i]);
            }

        }
        return sb.toString();
    }
}
