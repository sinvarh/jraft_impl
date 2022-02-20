import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import main.config.Metadata;
import main.config.RaftThreadPoolExecutor;
import main.constant.CommandType;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.AppendEntriesResp;
import main.model.rpc.common.RaftRpcReq;
import main.rpc.RaftRpcClient;

import java.util.*;
import java.util.concurrent.*;

import static main.config.NodeStatus.LEADER;

@Slf4j
public class TestExecutor {

    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    static volatile long current;
    public static void main(String[] args) {
        RaftThreadPoolExecutor raftThreadPoolExecutor;
        //todo 优化参数
        raftThreadPoolExecutor = new RaftThreadPoolExecutor(10, 10, 600,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());

        ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(1);
        Set<main.entity.Peer> peerSet = new HashSet<>();
        peerSet.add(new main.entity.Peer("127.0.0.1:9001"));
        peerSet.add(new main.entity.Peer("127.0.0.1:9002"));
        peerSet.add(new main.entity.Peer("127.0.0.1:9003"));


        heartbeatExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("moehrt");

                List<Callable<Boolean>> callableList = new ArrayList<>(peerSet.size());
                for (main.entity.Peer peer : peerSet) {
                    callableList.add(heartBeatResult(peer));
                }
                try {
                    List<Future<Boolean>> resList = raftThreadPoolExecutor.invokeAll(callableList);
                    for(Future<Boolean> heartBeatRes :resList){
                        if(heartBeatRes.get()){
                            //todo 重试
                            log.error("error");
                        };
                    }
                } catch (Exception e) {
                    log.error("heart invoke all error");
                }
            }
        },0,1,TimeUnit.SECONDS);

    }
    private static Callable<Boolean> heartBeatResult(main.entity.Peer p){
        return () -> {
            AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();
            appendEntriesReqs.setTerm(Metadata.currentTerm);
            appendEntriesReqs.setLeaderId(Metadata.voteFor);
            appendEntriesReqs.setLeaderCommit(Metadata.commitIndex);

            RaftRpcReq req = new RaftRpcReq(CommandType.appendLog.getType(), appendEntriesReqs);
            try {
                AppendEntriesResp appendEntriesResp = (AppendEntriesResp) RaftRpcClient.getClient().invokeSync(p.getAddr(), req, 3000);
                if(appendEntriesResp.getSuccess()){


                    return true;
                }
            } catch (Exception e) {
                log.error("心跳远程调用error", e);
            }

            return false;
        };
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


