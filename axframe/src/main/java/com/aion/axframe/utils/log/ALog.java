package com.aion.axframe.utils.log;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Aion on 16/9/20.
 */
public class ALog {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";

    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "ALog";
    private static final String SUFFIX = ".java";
    public static final int JSON_INDENT = 4;

    public static final int V = 0x1;
    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;

    private static final int DEFAULT = 0x1;
    private static final int JSON = 0x2;
    private static final int XML = 0x3;

    private static final int STACK_TRACE_INDEX = 5;

    private static String mGlobalTag;
    private static boolean mIsGlobalTagEmpty = true;
    private static boolean IS_SHOW_LOG = true;

    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void init(boolean isShowLog, @Nullable String tag) {
        IS_SHOW_LOG = isShowLog;
        mGlobalTag = tag;
        mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag);
    }

    public static void v() {
        printLog(DEFAULT, V, null, false, false, 0, 0, DEFAULT_MESSAGE);
    }

    public static void v(boolean isShowHeadInfo) {
        printLog(DEFAULT, V, null, isShowHeadInfo, true, 5, 0, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(DEFAULT, V, null, false, false, 0, 0, msg);
    }

    public static void v(boolean isShowHeadInfo, Object msg) {
        printLog(DEFAULT, V, null, isShowHeadInfo, true, 5, 0, msg);
    }

    public static void v(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, Object msg) {
        printLog(DEFAULT, V, null, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void v(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, Object msg) {
        printLog(DEFAULT, V, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(DEFAULT, V, tag, false, false, 0, 0, objects);
    }

    public static void d() {
        printLog(DEFAULT, D, null, false, false, 0, 0, DEFAULT_MESSAGE);
    }

    public static void d(boolean isShowHeadInfo) {
        printLog(DEFAULT, D, null, isShowHeadInfo, true, 5, 0, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(DEFAULT, D, null, false, false, 0, 0, msg);
    }

    public static void d(boolean isShowHeadInfo, Object msg) {
        printLog(DEFAULT, D, null, isShowHeadInfo, true, 5, 0, msg);
    }

    public static void d(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, Object msg) {
        printLog(DEFAULT, D, null, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void d(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, Object msg) {
        printLog(DEFAULT, D, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(DEFAULT, D, tag, false, false, 0, 0, objects);
    }

    public static void i() {
        printLog(DEFAULT, I, null, false, false, 0, 0, DEFAULT_MESSAGE);
    }

    public static void i(boolean isShowHeadInfo) {
        printLog(DEFAULT, I, null, isShowHeadInfo, true, 5, 0, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(DEFAULT, I, null, false, false, 0, 0, msg);
    }

    public static void i(boolean isShowHeadInfo, Object msg) {
        printLog(DEFAULT, I, null, isShowHeadInfo, true, 5, 0, msg);
    }

    public static void i(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, Object msg) {
        printLog(DEFAULT, I, null, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void i(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, Object msg) {
        printLog(DEFAULT, I, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(DEFAULT, I, tag, false, false, 0, 0, objects);
    }

    public static void w() {
        printLog(DEFAULT, W, null, false, false, 0, 0, DEFAULT_MESSAGE);
    }

    public static void w(boolean isShowHeadInfo) {
        printLog(DEFAULT, W, null, isShowHeadInfo, true, 5, 0, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(DEFAULT, W, null, false, false, 0, 0, msg);
    }

    public static void w(boolean isShowHeadInfo, Object msg) {
        printLog(DEFAULT, W, null, isShowHeadInfo, true, 5, 0, msg);
    }

    public static void w(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, Object msg) {
        printLog(DEFAULT, W, null, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void w(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, Object msg) {
        printLog(DEFAULT, W, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(DEFAULT, W, tag, false, false, 0, 0, objects);
    }

    public static void e() {
        printLog(DEFAULT, E, null, false, false, 0, 0, DEFAULT_MESSAGE);
    }

    public static void e(boolean isShowHeadInfo) {
        printLog(DEFAULT, E, null, isShowHeadInfo, true, 5, 0, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(DEFAULT, E, null, false, false, 0, 0, msg);
    }

    public static void e(boolean isShowHeadInfo, Object msg) {
        printLog(DEFAULT, E, null, isShowHeadInfo, true, 5, 0, msg);
    }

    public static void e(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, Object msg) {
        printLog(DEFAULT, E, null, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void e(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, Object msg) {
        printLog(DEFAULT, E, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(DEFAULT, E, tag, false, false, 0, 0, objects);
    }

    public static void a() {
        printLog(DEFAULT, A, null, false, false, 0, 0, DEFAULT_MESSAGE);
    }

    public static void a(boolean isShowHeadInfo) {
        printLog(DEFAULT, A, null, isShowHeadInfo, true, 5, 0, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(DEFAULT, A, null, false, false, 0, 0, msg);
    }

    public static void a(boolean isShowHeadInfo, Object msg) {
        printLog(DEFAULT, A, null, isShowHeadInfo, true, 5, 0, msg);
    }

    public static void a(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, Object msg) {
        printLog(DEFAULT, A, null, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void a(boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, Object msg) {
        printLog(DEFAULT, A, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, msg);
    }

    public static void a(String tag, Object... objects) {
        printLog(DEFAULT, A, tag, false, false, 0, 0, objects);
    }

    public static void json(String jsonFormat) {
        printLog(JSON, D, null, false, false, 0, 0, jsonFormat);
    }

    public static void json(int logtype, String jsonFormat) {
        printLog(JSON, logtype, null, false, false, 0, 0, jsonFormat);
    }

    public static void json(int logtype, boolean isShowHeadInfo, String jsonFormat) {
        printLog(JSON, logtype, null, isShowHeadInfo, true, 5, 0, jsonFormat);
    }

    public static void json(int logtype, String tag, String jsonFormat) {
        printLog(JSON, logtype, tag, false, false, 0, 0, jsonFormat);
    }

    public static void json(int logtype, boolean isShowHeadInfo, String tag, String jsonFormat) {
        printLog(JSON, logtype, tag, isShowHeadInfo, true, 5, 0, jsonFormat);
    }

    public static void json(int logtype, boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, String jsonFormat) {
        printLog(JSON, logtype, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, jsonFormat);
    }

    public static void xml(String xml) {
        printLog(XML, D, null, false, false, 0, 0, xml);
    }

    public static void xml(int logtype, String xml) {
        printLog(XML, logtype, null, false, false, 0, 0, xml);
    }

    public static void xml(int logtype, boolean isShowHeadInfo, String xml) {
        printLog(XML, logtype, null, isShowHeadInfo, true, 5, 0, xml);
    }

    public static void xml(int logtype, String tag, String xml) {
        printLog(XML, logtype, tag, false, false, 0, 0, xml);
    }

    public static void xml(int logtype, boolean isShowHeadInfo, String tag, String xml) {
        printLog(XML, logtype, tag, isShowHeadInfo, true, 5, 0, xml);
    }

    public static void xml(int logtype, boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, String tag, String xml) {
        printLog(XML, logtype, tag, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset, xml);
    }

    public static void file(File targetDirectory, Object msg) {
        printFile(null, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }

    /**
     * @param type 打印类型：1为普通Log；2为Json；3为XML
     * @param logtype 打印等级（V、D、I、W、E、A）
     * @param tagStr Log头标记tag
     * @param isShowHeadInfo 是否显示线程、堆栈信息的总开关
     * @param isShowThreadInfo 是否显示线程信息（只有总开关为true时，该字段才会发挥作用）
     * @param methodCount 要显示的堆栈信息数量（只有总开关为true时，该字段才会发挥作用）
     * @param methodOffset 要显示的堆栈信息偏移量（有几条信息不显示，只有总开关为true时，该字段才会发挥作用）
     * @param objects 要打印的Log对象
     */
    private static void printLog(int type, int logtype, String tagStr, boolean isShowHeadInfo, boolean isShowThreadInfo, int methodCount,int methodOffset, Object... objects) {
        if (!IS_SHOW_LOG) {
            return;
        }
        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        if (objects.length==1 && objects[0] instanceof Throwable) {
            BaseLog.printLog(E, tag, headString + msg, false, false, 0, 0);
        }
        switch (type) {
            case DEFAULT:
                BaseLog.printLog(logtype, tag, headString + msg, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset);
                break;
            case JSON:
                JsonLog.printJson(logtype, tag, msg, headString, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset);
                break;
            case XML:
                XmlLog.printXml(logtype, tag, msg, headString, isShowHeadInfo, isShowThreadInfo, methodCount, methodOffset);
                break;
        }
    }

    private static void printFile(String tagStr, File targetDirectory, String fileName, Object objectMsg) {
        if (!IS_SHOW_LOG) {
            return;
        }
        String[] contents = wrapperContent(tagStr, objectMsg);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        FileLog.printFile(tag, targetDirectory, fileName, headString, msg);
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }
        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String tag = (tagStr == null ? className : tagStr);
        if (mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            tag = TAG_DEFAULT;
        } else if (!mIsGlobalTagEmpty) {
            tag = mGlobalTag;
        }
        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object... objects) {
        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            if (object instanceof Throwable) {
                StringBuilder sb = new StringBuilder();
                Writer writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                ((Throwable) object).printStackTrace(pw);
                Throwable cause = ((Throwable) object).getCause();
                // 循环着把所有的异常信息写入writer中
                while (cause != null) {
                    cause.printStackTrace(pw);
                    cause = cause.getCause();
                }
                pw.close();// 记得关闭
                String result = writer.toString();
                return sb.append(result).toString();
            }
            return object == null ? NULL : object.toString();
        }
    }
}
