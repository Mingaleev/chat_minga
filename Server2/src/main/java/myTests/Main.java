package myTests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        TestMetods tm = new TestMetods();
        StartTest.start(tm.getClass());
    }
}
