package com.example.myapp.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanZiToPinYin {

    /**
     * @param pinyinArray
     * @return
     */
    public static String concatPinyinStringArray(String[] pinyinArray) {
        StringBuffer pinyinStrBuf = new StringBuffer();

        if ((null != pinyinArray) && (pinyinArray.length > 0)) {
            for (int i = 0; i < pinyinArray.length; i++) {
                pinyinStrBuf.append(pinyinArray[i]);
                pinyinStrBuf.append(System.getProperty("line.separator"));
            }
        }
        String outputString = pinyinStrBuf.toString();
        return outputString;
    }

    //多音字怎么处理




    /**
     * @Title: 获取中文串拼音首字母，英文字符不变
     * @methodName:  getFirstSpell
     * @param chinese 汉字串
     * @return java.lang.String 中文拼音首字母
     * @Description:
     *
     * @author: FLY
     * @date:  2017-11-13 17:13
     */
    public static String getFirstSpell(String chinese) {


        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * @Title:  获取中文串拼音，英文字符不变
     * @methodName:  getFullSpell
     * @param chinese 中文字符串
     * @return java.lang.String  中文拼音
     * @Description:
     *
     * @author: FLY
     * @date:  2017-11-13 17:15
     */
    public static String getFullSpell(String chinese) {

        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }
}
