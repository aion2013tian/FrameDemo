package com.aion.axframe.utils.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aion on 16/9/23.
 */
public class JsonLog {

    public static void printJson(int type, String tag, String msg, String headString, boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount, int methodOffset) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(ALog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(ALog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        LineUtilsLog.printLine(type, tag, LineUtilsLog.TOP);
        if (isShowHeadInfo) {
            LineUtilsLog.logHeaderContent(type, tag, isShowThreadInfo, methodCount, methodOffset);
        }
        message = headString + ALog.LINE_SEPARATOR + message;
        String[] lines = message.split(ALog.LINE_SEPARATOR);
        for (String line : lines) {
            LineUtilsLog.typeLog(type, tag, "║ " + line);
        }
        LineUtilsLog.printLine(type, tag, LineUtilsLog.BOM);
    }

    public static void printJson(String tag, String msg, String headString) {
        printJson(ALog.D, tag, msg, headString, false, false, 0, 0);
    }
}
