package com.github.klee0kai.stone.utils;

public class ExceptionUtils {

    public static String collectCauseMessages(String mes, Throwable cause) {
        StringBuilder sb = new StringBuilder(mes);
        while (cause != null) {
            if (sb.length() > 0) sb.append("Caused by: ");
            sb.append(cause.getMessage()).append("\n");
            cause = cause.getCause();
        }
        return sb.toString();
    }

}
