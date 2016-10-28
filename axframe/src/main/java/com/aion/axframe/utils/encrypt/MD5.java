package com.aion.axframe.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MD5加密
 * @author jiang.li
 * @date 2013-12-17 14:09
 */
public class MD5 {

	 /**全局数组**/
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

    /**
     * 返回形式为数字跟字符串
     * @param bByte
     * @return
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     * @param bByte
     * @return
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * MD5加密
     * @param str 待加密的字符串
     * @return
     */
    public static String encrypt(String str) {
        String result = null;
        try {
        	result = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byteToString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    /**
     * MD5加密
     * @param str 待加密的字符串
     * @param lowerCase 大小写
     * @return
     */
    public static String encrypt(String str,boolean lowerCase) {
        String result = null;
        try {
        	result = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byteToString(md.digest(str.getBytes()));
            if(lowerCase){
            	result = result.toLowerCase();	
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static HashMap<String,String> sortMapByValues(Map<String, String> aMap) {

        Set<Map.Entry<String, String>> mapEntries = aMap.entrySet();
        List<Map.Entry<String, String>> aList = new LinkedList<>(mapEntries);
        Collections.sort(aList, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> ele1,
                               Map.Entry<String, String> ele2) {

                return ele1.getKey().compareTo(ele2.getKey());
            }
        });

        // Storing the list into Linked HashMap to preserve the order of insertion.
        HashMap<String, String> aMap2 = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : aList) {
            aMap2.put(entry.getKey(), entry.getValue());
        }

        return aMap2;
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                resultString = byteToString(md.digest(strObj.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}
