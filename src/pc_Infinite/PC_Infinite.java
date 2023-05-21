package pc_Infinite;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class PC_Infinite {
    public static void main(String[] args) {
        PC pc = new PC();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                pc.producer();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                pc.consumer();
            }
        });

        // 자바에서 쓰레드 병렬로 실행
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> { t1.run(); });
        executor.submit(() -> { t2.run(); });
    }
}

class PC {
    int itemNume = 0;
    // 생산자가 먼저 생산할 수 있게 생산자 세마포어는 1, 소비자 세마포어는 0으로 선언
    static Semaphore semCon = new Semaphore(0); // n
    static Semaphore semProd = new Semaphore(1); // s

    void producer() {
        while (true) {
            try {
                semProd.acquire();
                System.out.println("생산자 실행, 아이템 갯수 : " + ++itemNume);
                semProd.release();
                semCon.release();

            } catch (InterruptedException e) {
                System.out.println("버퍼가 가득 차있어서 생산자가 생산하지 못함");
            }
        }
    }

    void consumer() {
        while (true) {
            try {
                semCon.acquire(); // 세마포어가 1 이상이여야 작동하고, 아니면 작동하지 않음
                semProd.acquire();
                System.out.println("소비자 실행, 아이템 갯수 : " + --itemNume);
                semProd.release();
            } catch (InterruptedException e) {
                System.out.println("버퍼가 비어있어서 소비자가 소비하지 못함");
            }
        }
    }
}
