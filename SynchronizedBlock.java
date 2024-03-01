import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SynchronizedBlock {
    public static void main(String[] args) throws InterruptedException {
        new Worker().main();
    }
}

class Worker {
    Object lock1 = new Object();
    Object lock2 = new Object();

    Random random = new Random();
    List<Integer> list1 = new ArrayList<>();
    List<Integer> list2= new ArrayList<>();

    public void addToListOne(){
        synchronized (lock1) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            list1.add(random.nextInt(100));
        }
    }

    public void addToListTwo(){
        synchronized (lock2) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            list2.add(random.nextInt(100));
        }
    }

    public void work(){
        for (int i = 0; i < 1000; i++) {
            addToListOne();
            addToListTwo();
        }
    }
    public void main(){
        long before = System.currentTimeMillis();

        Thread one = new Thread(new Runnable() {
            @Override
            public void run() {
                work();
            }
        });

        Thread two = new Thread(new Runnable() {
            @Override
            public void run() {
                work();
            }
        });

        one.start();
        two.start();
        try {
            one.join();
            two.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long after = System.currentTimeMillis();

        System.out.println(after - before);

        System.out.println("List1 " + list1.size());
        System.out.println("List2 " + list2.size());
    }

}
