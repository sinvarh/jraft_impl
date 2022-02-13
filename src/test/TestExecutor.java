import lombok.SneakyThrows;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestExecutor {

    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    static volatile long current;
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        current = System.currentTimeMillis();
        executor.scheduleAtFixedRate(
                new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        Thread.sleep(1000);
                        System.out.println("aaaa");
                        System.out.println(System.currentTimeMillis() - current);
                        current = System.currentTimeMillis();
                    }
                },
                5,
                5,
                TimeUnit.SECONDS);

        executor.scheduleAtFixedRate(
                new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        Thread.sleep(1000);
                        System.out.println("bbbb");
                        System.out.println(System.currentTimeMillis() - current);
                        current = System.currentTimeMillis();
                    }
                },
                5,
                5,
                TimeUnit.SECONDS);


    }




    public static void testFixedDelay(){
        executor.scheduleWithFixedDelay(new myRun(), 5, 5, TimeUnit.SECONDS);
    }


    public static class myRun implements Runnable{

        @Override
        public void run() {
            System.out.println("----测试开始--------"+ new Date().toLocaleString());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("---休眠3秒后, 处理结束--------"+new Date().toLocaleString());
        }
    }


}


