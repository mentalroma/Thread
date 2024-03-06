import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Dead_lock {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();

        Thread one = new Thread(new Runnable() {
            @Override
            public void run() {
                runner.firstThread();
            }
        });

        Thread two = new Thread(new Runnable() {
            @Override
            public void run() {
                runner.secondThread();
            }
        });

        one.start();
        two.start();

        one.join();
        two.join();

        runner.finished();
    }
}

class Runner{
    private Account acc1 = new Account();
    private Account acc2 = new Account();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    private Random random = new Random();

    public void firstThread(){

        for (int i = 0; i < 10000; i++) {
            takeLocks(lock1, lock2);

            try {
                Account.transfer(acc1, acc2, random.nextInt(100));
            }finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void secondThread(){

        for (int i = 0; i < 10000; i++) {
            takeLocks(lock2, lock1);

            try {
                Account.transfer(acc2, acc1, random.nextInt(100));
            }finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    private void takeLocks(Lock lock1, Lock lock2){
        boolean firstLockTaken = false;
        boolean secondLockTaken = false;


        while(true) {
            try {
                firstLockTaken = lock1.tryLock();
                secondLockTaken = lock2.tryLock();
            } finally {
                if (firstLockTaken && secondLockTaken) {
                    return;
                }

                if (firstLockTaken) {
                    lock1.unlock();
                }

                if (secondLockTaken) {
                    lock2.unlock();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void finished(){
        System.out.println(acc1.getBalance());
        System.out.println(acc2.getBalance());

        System.out.println("Total balance " + (acc1.getBalance() +  acc2.getBalance()));
    }
}

class Account{
    private int balance = 10000;

    public void deposit(int amount){
        balance+=amount;
    }

    public void withraw(int amount){
        balance-=amount;
    }

    public int getBalance(){
        return balance;
    }

    public static void transfer(Account acc1, Account acc2, int amount){
        acc1.withraw(amount);
        acc2.deposit(amount);
    }
}


