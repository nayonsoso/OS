package pc_bounded;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class PC_Bounded {
    public static void main(String[] args) {
        PC pc = new PC(10); // 버퍼의 크기가 10이라 가정

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
        executor.submit(() -> {
            t1.run();
        });
        executor.submit(() -> {
            t2.run();
        });
    }
}

class PC {
    // 생산자가 먼저 생산할 수 있게 생산자 세마포어는 1, 소비자 세마포어는 0으로 선언
    static Semaphore semCon = new Semaphore(0); // n
    static Semaphore semProd = new Semaphore(1); // s
    static LinkedList ll = new LinkedList();
    static Semaphore semSize = new Semaphore(5);// e
    final int size;
    int itemNum = 0;
    PC(int size) {
        this.size = size;
    }

    void producer() {
        while (true) {
            try {
                semSize.acquire();
                semProd.acquire();
                ll.add(1);
                System.out.println("생산자 실행, 아이템 갯수 : " + ll.size());
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
                semCon.acquire();
                semProd.acquire();
                ll.remove(0);
                System.out.println("소비자 실행, 아이템 갯수 : " + ll.size());
                semProd.release();
                semSize.release();
            } catch (InterruptedException e) {
                System.out.println("버퍼가 비어있어서 소비자가 소비하지 못함");
            }
        }
    }
}
