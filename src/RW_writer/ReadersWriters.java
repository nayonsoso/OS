

package RW_writer;

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
        int writeCount = 0;

        // writer가 쓰려는 의사를 밝히면, 그 후 들어오는 reader을 rsem을 통해 기다리게 함
        static Semaphore semX = new Semaphore(1); // reader의 상호배제를 위한 lock
        static Semaphore semY = new Semaphore(1); // writer의 상호배제를 위한 lock
        static Semaphore semZ = new Semaphore(1); // 멀티플한 readers를 막기 위한 용도
        static Semaphore semW = new Semaphore(1); // writer에 대한 lock
        static Semaphore semR = new Semaphore(1); // reader에 대한 lock

        static LinkedList ll = new LinkedList();
        int itemNum = 0;

        void reader() {
            for (int i = 0; i < ITER; i++) {
                try {
                    // reader가 하나 이상이면 writer에 대한 lock 걸어줌
                    semZ.acquire();
                    semR.acquire();
                    semX.acquire();
                    readCount++;
                    if(readCount==1){
                        semW.acquire();
                    }
                    semX.release();
                    semR.release();
                    semZ.release();

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
                    semY.acquire();
                    writeCount++;
                    if(writeCount==1){
                        semR.acquire(); // writer가 접근 의사 밝히면 리더 lock
                    }
                    semY.release();

                    semW.acquire();
                    ll.add(++itemNum);
                    System.out.println("Writer 실행, 추가한 아이템 : " + itemNum);
                    semW.release();

                    semY.acquire();
                    writeCount--;
                    if(writeCount==0){
                        semR.release();
                    }
                    semY.release();
                } catch (InterruptedException e) {
                }
            }
        }
    }
}

