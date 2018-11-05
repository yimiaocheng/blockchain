package com.cwt.app.common.util;

public class ExceptionUtil {

    private void ExceptionUtil() {
    }

    /**
     * 输出异常信息
     * @param e
     * @return
     */
    public static String readStackTrace(Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.getClass().getName()).append("：").append(e.getMessage()).append("\r\n");
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            stringBuilder.append("\tat ")
                    .append(stackTraceElement.toString())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
