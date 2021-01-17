import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MainTest {

    private int[] a;
    private int[] b;
    Four four;

    public MainTest(int[] a, int[] b) {
        this.a = a;
        this.b = b;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{5, 1, 9}, new int[]{1,8,4,9, 4, 5, 1, 9}},
                {new int[]{1, 9}, new int[]{3, 8, 9, 4, 1, 9}},
                {new int[]{113, 990}, new int[]{117, 4, 56, 4, 113, 990}},
                {new int[]{10, 55, 66, 77}, new int[]{10, 10, 4, 4, 10, 55, 66, 77}},
        });
    }

    @Before
    public void init() {
        four = new Four();
    }

    @Test
    public void test() {
        Assert.assertArrayEquals(a, four.afterLastFour(b));
    }
}
