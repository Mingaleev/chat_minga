package racing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static String winCar = null;
    private static final Object mon = new Object();


    public static void main(String[] args) {

        CountDownLatch cdl1 = new CountDownLatch(CARS_COUNT);
        CountDownLatch cdl2 = new CountDownLatch(CARS_COUNT);
        Semaphore smp = new Semaphore(CARS_COUNT/2);

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(smp), new Road(40));

        List<Car> cars = new ArrayList<>();


        for (int i = 0; i < CARS_COUNT; i++) {
            cars.add(new Car(race, 20 + (int) (Math.random() * 10),cdl1,cdl2));
        }

        for (int i = 0; i < CARS_COUNT; i++) {
            new Thread(cars.get(i)).start();
        }

        try {
            cdl1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            cdl2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }

    public static void win (String car) {
        synchronized (mon) {
            if (winCar == null) {
                winCar = car;
                System.out.println(car);
            }
        }
    }

}
