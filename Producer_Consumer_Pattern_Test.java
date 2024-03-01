import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Producer_Consumer_Pattern_Test {
    private static BlockingQueue<Integer> blockingQueue =
            new ArrayBlockingQueue<>(10);

    public static void main(String[] args)  {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    comsumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void produce() throws InterruptedException {
        Random random = new Random();

        while (true){
            blockingQueue.put(random.nextInt(100));
        }
    }

    private static void comsumer() throws InterruptedException {
        Random random = new Random();

        while(true){
            Thread.sleep(100);

            if(random.nextInt(10) == 5){
            System.out.println(blockingQueue.take());
            System.out.println("Queue size is : " + blockingQueue.size());
            }
        }
    }
}

