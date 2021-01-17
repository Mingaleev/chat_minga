

import static java.util.Arrays.copyOfRange;

public class Four {

    public int[] afterLastFour(int[] a) {
        int check = 0;

        for (int i = 0; i < a.length; i++) {
            if (a[i] == 4) {
                check = i + 1;
            }
        }

        if (check == 0) {
            throw new RuntimeException();
        }

        return copyOfRange (a,check,a.length);
    }
}
