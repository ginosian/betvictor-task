package com.task.betvictortask.aop;

public class ProcessingTimeHolder {

    private static final ThreadLocal<Long> TIME = new ThreadLocal<>();

    public static Long getTime() {
        return TIME.get();
    }

    public static void setTime(long time) {
        TIME.set(time);
    }

    public static void clear() {
        TIME.remove();
    }
}

