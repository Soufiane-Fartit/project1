package org.example.utils;

public class Strings {
    public static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
