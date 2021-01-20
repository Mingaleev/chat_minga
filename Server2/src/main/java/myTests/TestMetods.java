package myTests;

public class TestMetods {

    @BeforeSuite
    public static void BeforeSuite() {
        System.out.println("BeforeSuite");
    }

    @Test(priority = 1)
    public static void test1() {
        System.out.println("test1 - priority = 1");
    }

    @Test(priority = 9)
    public static void test2() {
        System.out.println("test2 - priority = 9");
    }

    @Test
    public static void test3() {
        System.out.println("test3 - default priority = 5");
    }

    @Test
    public static void test4() {
        System.out.println("test4 - default priority = 5");
    }

    @Test(priority = 4)
    public static void test5() {
        System.out.println("test5 - priority = 4");
    }

    @AfterSuite
    public static void AfterSuite() {
        System.out.println("AfterSuite");
    }

}
