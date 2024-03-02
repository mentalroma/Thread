import java.util.LinkedList;
import java.util.Queue;

public class Producer_Consumer_Pattern_With_Wait_Notifiy {

    public static void main(String[] args) {
        ProducerConsumer producerConsumer = new ProducerConsumer();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    producerConsumer.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    producerConsumer.consume();
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
}

class ProducerConsumer {
    private Queue<Integer> queue = new LinkedList<Integer>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    public void produce() throws InterruptedException {
        int value = 0;
       while(true){
           synchronized (lock){
               while(queue.size() == LIMIT){
                   lock.wait();
               }

               queue.offer(value++);
               lock.notify();
           }
       }
    }

    public void consume() throws InterruptedException{
        int value = 0;
        while(true){
            synchronized (lock){
                while(queue.size() == 0){
                    lock.wait();
                }

                value = queue.poll();
                System.out.println(value);
                System.out.println("size = " + queue.size());
                lock.notify();
            }
            Thread.sleep(1000);
        }
    }
}

