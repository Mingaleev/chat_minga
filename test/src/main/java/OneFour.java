
public class OneFour {

//    public static void main(String[] args) {
////        System.out.println(CheckOneFour(new int[]{41,41,41,1,44}));
//    }

    public boolean CheckOneFour(int[] a) {
        boolean check1 = false;
        boolean check4 = false;
        for (int j : a) {
            if (j == 1) {
                check1 = true;
            } else if (j == 4) {
                check4 = true;
            } else {
                return false;
            }
        }
        return check1 && check4;
    }
}
