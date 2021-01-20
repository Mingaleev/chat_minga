package myTests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StartTest {
    static void start(Class testClass) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = testClass.getDeclaredMethods();
        List<Method> testMetods = new ArrayList<>();
        Method methodBeforeSuite = null;
        Method methodAfterSuite = null;
        int sumMethodsBeforeSuite = 0;
        int sumMethodsAfterSuite = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                methodBeforeSuite = method;
                sumMethodsBeforeSuite++;
            }

            if (method.isAnnotationPresent(AfterSuite.class)) {
                methodAfterSuite = method;
                sumMethodsAfterSuite++;
            }

            if (method.isAnnotationPresent(Test.class)) {
                testMetods.add(method);
            }
        }
        if (sumMethodsBeforeSuite == 1 && sumMethodsAfterSuite == 1) {
            methodBeforeSuite.invoke(null);
        } else if(sumMethodsBeforeSuite > 1 || sumMethodsAfterSuite > 1) {
            throw new RuntimeException("Больше 2х методов BeforeSuite или AfterSuite");
        }
        testMetods.sort(Comparator.comparingInt((Method i) -> i.getAnnotation(Test.class).priority()).reversed());

        for (Method method: testMetods) {
            method.invoke(null);
        }



        methodAfterSuite.invoke(null);
    }
}
