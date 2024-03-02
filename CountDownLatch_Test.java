import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatch_Test {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3); /* Потоко защищенный класс
        который отсчитывает числа назад, и если отсчитались до нуля открывается защелка
        и пропускает код дальше. 3 - размер дикримента
        */

        //Пул потоков
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //Передаем работу всем трем потокам
        for (int i = 0; i < 3; i++) {
            executorService.submit(new Processor(i, countDownLatch));
        }

        //Прекращаем submit новых заданий
        executorService.shutdown();

        //Каждую секунду делаем отнимаем от дикримента
        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            countDownLatch.countDown();
        }
    }
}
class Processor implements Runnable{
    private CountDownLatch countDownLatch;
    private int id;
    public Processor(int id,CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            // Ждем когда countDownLatch будет нулем
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Все прошло
        System.out.println("Thread with id : " + id + "proceeded.");
    }
}
