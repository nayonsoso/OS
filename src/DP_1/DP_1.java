

package DP_1;

import java.util.concurrent.Semaphore;

public class DP_1 {

}

class Philosopher extends Thread {
    int id;
    Semaphore lfork, rfork, once;
    Philosopher(int id, Semaphore lfork, Semaphore rfork, Semaphore once) {
        this.id = id;
        this.lfork = lfork;
        this.rfork = rfork;
        this.once = once;
    }

    public void run() {
        try {
            while (true) {
                once.acquire();
                lfork.acquire();
                rfork.acquire();
                once.release();
                eating();
                lfork.release();
                rfork.release();
                thinking();
            }
        }catch (InterruptedException e) { }
    }
    void eating() {
        System.out.println("[" + id + "] eating");
    }
    void thinking() {
        System.out.println("[" + id + "] thinking");
    }
}

class DiningPhilosophersTest {
    static final int num = 5;
    public static void main(String[] args) {
        int i;
        /* forks */
        Semaphore[] fork = new Semaphore[num];
        for (i=0; i<num; i++){
            fork[i] = new Semaphore(1);
        }
        Semaphore once = new Semaphore(1);
        /* philosophers */
        Philosopher[] phil = new Philosopher[num];
        for (i=0; i<num; i++) {
            phil[i] = new Philosopher(i, fork[i], fork[(i+1)%num], once);
            // 5번째 철학자는 5번 포크와 1번 포크를 잡으므로 % 연산 필요
        }
        for (i=0; i<num; i++) {
            phil[i].start();
        }
    }
}
