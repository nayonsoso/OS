package PC_bounded;

import java.util.concurrent.Semaphore;

public class PC_Bounded {
    static int x = 0;
    static int ITER = 1000;

    public static void main(String[] args) {
        PC pc = new PC();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() { pc.producer(); }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                pc.consumer();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupt occurred.");
        }

        if (x != 0) {
            System.out.println("BOOM! counter=" + x);
        } else {
            System.out.println("OK counter=" + x);
        }
    }

    static class PC {
        // 생산자가 먼저 생산할 수 있게 생산자 세마포어는 1, 소비자 세마포어는 0으로 선언
        static Semaphore semCon = new Semaphore(0); // n
        static Semaphore semProd = new Semaphore(1); // s
        static Semaphore semSize = new Semaphore(5);// e

        void producer() {
            for (int i = 0; i < ITER; i++) {
                try {
                    semSize.acquire();
                    semProd.acquire();
                    x++;
                    System.out.println(Thread.currentThread().getId() + ": " + x);
                    semProd.release();
                    semCon.release();
                } catch (InterruptedException e) {
                }
            }
        }

        void consumer() {
            for (int i = 0; i < ITER; i++) {
                try {
                    semCon.acquire();
                    semProd.acquire();
                    x--;
                    System.out.println(Thread.currentThread().getId() + ": " + x);
                    semProd.release();
                    semSize.release();
                } catch (InterruptedException e) {
                }
            }
        }
    }
}

