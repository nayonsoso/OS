import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsynThread {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Job1 " + threadName);
        });
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Job2 " + threadName);
        });
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Job3 " + threadName);
        });
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Job4 " + threadName);
        });

        executor.shutdown();

    }
}