package com.aion.axframe.utils.log;

/**
 * Created by Aion on 16/9/20.
 */
public class BaseLog {

    public static void printDefault(int type, String tag, String msg) {
        printLog(type, tag, msg, false, false, 0, 0);
    }

    public static void printLog(int type, String tag, String msg, boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount, int methodOffset) {
        int index = 0;
        int maxLength = 3990;
        int countOfSub = msg.length() / maxLength;
        if (isShowHeadInfo) {
            LineUtilsLog.printLine(type, tag, LineUtilsLog.TOP);
            LineUtilsLog.logHeaderContent(type, tag, isShowThreadInfo, methodCount, methodOffset);
        }

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                if (isShowHeadInfo) {
                    LineUtilsLog.typeLog(type, tag, "║ " + sub);
                } else {
                    LineUtilsLog.typeLog(type, tag, sub);
                }
                index += maxLength;
            }
            LineUtilsLog.typeLog(type, tag, msg.substring(index, msg.length()));
        } else {
            if (isShowHeadInfo) {
                LineUtilsLog.typeLog(type, tag, "║ " + msg);
            } else {
                LineUtilsLog.typeLog(type, tag, msg);
            }
        }
        if (isShowHeadInfo) {
            LineUtilsLog.printLine(type, tag, LineUtilsLog.BOM);
        }
    }
}
