package ex06;

/**
 * DO NOT EDIT OR REMOVE THIS FILE
 */
public class Tracer {
    static StringBuilder strTrace = new StringBuilder();
    private static int depth = 2;

    static void trace(Object obj) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        strTrace.append(obj.hashCode() + ":" + stackTraceElements[depth].getMethodName() + "\n");
    }

    static void trace() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        strTrace.append(stackTraceElements[depth].getClassName() + ":" + stackTraceElements[depth].getMethodName() + "\n");
    }

    public static void clear() {
        strTrace = new StringBuilder();
    }

    public static StringBuilder getTrace() {
        return strTrace;
    }
}
