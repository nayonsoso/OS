

package DP_2;

import java.util.concurrent.Semaphore;

public class DP_2 {

}

class Philosopher extends Thread {
    int id;
    Semaphore lfork, rfork;
    Philosopher(int id, Semaphore lfork, Semaphore rfork) {
        this.id = id;
        this.lfork = lfork;
        this.rfork = rfork;
    }
    public void run() {
        try {
            while (true) {
                if(id < 4) {
                    lfork.acquire();
                    rfork.acquire();
                }else {
                    rfork.acquire(); // r0
                    lfork.acquire(); // r4
                }
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
        for (i=0; i<num; i++) {
            fork[i] = new Semaphore(1);
        }
        /* philosophers */
        Philosopher[] phil = new Philosopher[num];
        for (i=0; i<num; i++) {
            phil[i] = new Philosopher(i, fork[i], fork[(i+1)%num]);
        }
        for (i=0; i<num; i++){
            phil[i].start();
        }
    }
}