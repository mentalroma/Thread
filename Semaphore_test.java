import java.util.concurrent.*;

public class Semaphore_test {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        Connection connection = Connection.getConnection();

        for (int i = 0; i < 200; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection.work();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }
}

class Connection{
    private static Connection connection = new Connection();
    private int connectionsCount = 0;
    private Semaphore semaphore = new Semaphore(10);

    private Connection(){

    }

    public static Connection getConnection(){
        return connection;

    }

    public void work() throws InterruptedException {
        semaphore.acquire();
        try{
            dowork();
        }finally {
            semaphore.release();
        }
    }

    public void dowork() throws InterruptedException {
        synchronized (this){
            connectionsCount++;
            System.out.println(connectionsCount);
        }

        Thread.sleep(5000);

        synchronized (this){
            connectionsCount--;
        }
    }
}
