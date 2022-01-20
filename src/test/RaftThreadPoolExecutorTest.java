import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import main.config.RaftThreadPoolExecutor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class RaftThreadPoolExecutorTest {
    private RaftThreadPoolExecutor raftThreadPoolExecutor;

    @Before
    public void setUp(){
        raftThreadPoolExecutor = new RaftThreadPoolExecutor(10,10,600,
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());
    }

    @Test
    public void testInvokeAll() throws InterruptedException {
        List<Callable<Boolean>> callableList = new ArrayList<>(100);
        for(int i =0;i<100;i++){
            int finalI = i;
            callableList.add(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println(finalI);
                    Thread.sleep(1000);
                    return true;
                }

            });
        }
        long time = System.currentTimeMillis();
        //test invoke all的时长，现在是不是开多线程去等待
        List<Future<Boolean>>  futures = raftThreadPoolExecutor.invokeAll(callableList);
        System.out.println(System.currentTimeMillis()-time);
    }
}
