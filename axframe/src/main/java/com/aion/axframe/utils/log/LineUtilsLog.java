package com.aion.axframe.utils.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Aion on 16/9/20.
 */
public class LineUtilsLog {

    public static final int TOP = 1;
    public static final int MID = 2;
    public static final int BOM = 3;
    private static final String STRTOP = "╔════════════════════════════════════════════════════════════════════════════════════════";
    private static final String STRMID = "╟────────────────────────────────────────────────────────────────────────────────────────";
    private static final String STRBOM = "╚════════════════════════════════════════════════════════════════════════════════════════";

    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(int type, String tag, int site) {
        String str = "";
        switch (site) {
            case TOP:
                str = STRTOP;
                break;
            case MID:
                str = STRMID;
                break;
            case BOM:
                str = STRBOM;
                break;
        }
        typeLog(type, tag, str);
    }

    public static void typeLog(int type, String tag, String str) {
        switch (type) {
            case ALog.V:
                Log.v(tag, str);
                break;
            case ALog.D:
                Log.d(tag, str);
                break;
            case ALog.I:
                Log.i(tag, str);
                break;
            case ALog.W:
                Log.w(tag, str);
                break;
            case ALog.E:
                Log.e(tag, str);
                break;
            case ALog.A:
                Log.wtf(tag, str);
                break;
        }
    }

    public static void printLine(String tag, int site) {
        printLine(ALog.D, tag, site);
    }

    public static void logHeaderContent(int logType, String tag, boolean isShowThreadInfo, int methodCount, int methodOffset) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if(isShowThreadInfo) {
            typeLog(logType, tag, "║ Thread: " + Thread.currentThread().getName());
            printLine(logType, tag, MID);
        }

        String level = "";
        int stackOffset = getStackOffset(trace) + methodOffset;
        if(methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for(int i = methodCount; i > 0; --i) {
            int stackIndex = i + stackOffset;
            if(stackIndex < trace.length) {
                StringBuilder builder = new StringBuilder();
                builder.append("║ ").append(level).append(getSimpleClassName(trace[stackIndex].getClassName())).append(".").append(trace[stackIndex].getMethodName()).append(" ").append(" (").append(trace[stackIndex].getFileName()).append(":").append(trace[stackIndex].getLineNumber()).append(")");
                level = level + "   ";
                typeLog(logType, tag, builder.toString());
            }
        }
        if (methodCount > 0) {
            printLine(logType, tag, MID);
        }

    }

    public static int getStackOffset(StackTraceElement[] trace) {
        for(int i = 5; i < trace.length; ++i) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if(!name.equals(LineUtilsLog.class.getName()) && !name.equals(ALog.class.getName())) {
                --i;
                return i;
            }
        }

        return -1;
    }

    public static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }
}
