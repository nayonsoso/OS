
package RW_reader;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ReadersWriters {
    static int ITER = 1000;

    public static void main(String[] args) {
        RW rw = new RW();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                rw.reader();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                rw.writer();
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
    }

    static class RW {
        int readCount = 0;
        // reader가 하나라도 있으면 writer을 배제하고 reader가 없으면 writer을 허용한다.
        // 이 전략은 reader가 계속 붙잡고 있을 수 있으므로 Reader가 더 유리
        static Semaphore semX = new Semaphore(1); // 상호 배제를 위한 세마포어
        static Semaphore semW = new Semaphore(1); // writer에 대한 lock

        static LinkedList ll = new LinkedList();
        int itemNum = 0;

        void reader() {
            for (int i = 0; i < ITER; i++) {
                try {
                    // reader가 하나 이상이면 writer에 대한 lock 걸어줌
                    semX.acquire();
                    readCount++;
                    if(readCount==1){
                        semW.acquire();
                    }
                    semX.release();
                    System.out.println("Reader 실행, 읽은 아이템 : " + ll.peekLast());

                    // read가 끝나면 readCount를 하나 줄여줌
                    semX.acquire();
                    readCount--;
                    if(readCount==0){ // reader가 없으면 writer 깨움
                        semW.release();
                    }
                    semX.release();
                } catch (InterruptedException e) {
                }
            }
        }

        void writer() {
            for (int i = 0; i < ITER; i++) {
                try {
                    semW.acquire();
                    ll.add(++itemNum);
                    System.out.println("Writer 실행, 추가한 아이템 : " + itemNum);
                    semW.release();
                } catch (InterruptedException e) {
                }
            }
        }
    }
}