import java.util.concurrent.locks.Lock;

public class ReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();

        //Первый поток
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //Вызываем метод инкримента для первого потока
                task.firtThread();
            }
        });

        //Второй поток
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //Вызываем метод инкримента для второго потока
                task.secondThread();
            }
        });

        thread1.start();
        thread2.start();


        thread1.join();
        thread2.join();

        task.showCounter();
    }
}

class Task {
    private int counter;

    /*
    Обьект класса ReentrantLock --
        Представляет собой аналогию блока scynzhronized
        .lock() - блокирует
        .unlock() - разблокир.

        сколько раз был вызван метод .lock, столько и .unlock методов надо будет вызвать
     */
    private Lock lock = new java.util.concurrent.locks.ReentrantLock();

    private void incriment(){
        for (int i = 0; i < 10000; i++) {
            counter++;
        }
    }

    public void firtThread(){
        lock.lock();
        incriment();
        lock.unlock();
    }

    public void secondThread(){
        lock.lock();
        incriment();
        lock.unlock();
    }

    public void showCounter(){
        System.out.println(counter);
    }
}
