import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OneFourTest {
    private int[] a;
    OneFour oneFour;

    public OneFourTest(int[] a) {
        this.a = a;
    }


    @Parameterized.Parameters
    public static Collection<int[]> data() {
        return Arrays.asList(new int[][]{
                {4, 5, 1, 9},
                {5, 1, 9, 4},
                {4,4,8,8,6,6,1},
                {1,1,1,1,1,1},
                {4,1,4,1,4,1}

        });
    }


    @Before
    public void init() {
        oneFour = new OneFour();
    }

    @Test
    public void test() {
        Assert.assertTrue(oneFour.CheckOneFour(a));
    }
}
