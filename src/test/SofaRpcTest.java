import com.alipay.remoting.exception.RemotingException;
import lombok.extern.slf4j.Slf4j;
import main.*;
import main.config.Metadata;
import main.config.RaftThreadPoolExecutor;
import main.constant.CommandType;
import main.entity.Peer;
import main.model.app.KVReqs;
import main.model.app.KVResp;
import main.model.log.LogEntry;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.AppendEntriesResp;
import main.model.rpc.common.RaftRpcReq;
import main.model.rpc.common.RaftRpcResp;
import main.rpc.RaftRpcClient;
import main.rpc.RaftRpcServer;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

import static main.config.NodeStatus.LEADER;

@Slf4j
public class SofaRpcTest {

    private RaftRpcServer raftRpcServer;
    private Consensus consensus;
    private LogModule logModule;
    private StateMachine stateMachine;
    private int serverPort;

    @Before
    public void setUp() {
        serverPort = 9081;
        logModule = new DefaultLogModule(serverPort);
        stateMachine = new StateMachineImpl(serverPort);
        consensus = new ConsensusImpl(logModule, stateMachine);
//        raftRpcServer = new RaftRpcServer(serverPort, );


    }

    @Test
    public void testAppendLog() throws RemotingException, InterruptedException {
        String addr = "127.0.0.1:" + serverPort;
        List<LogEntry> list = Collections.singletonList(new LogEntry(2L, 1, new LogEntry.Command("1111", "2222")));
        AppendEntriesReqs reqs = new AppendEntriesReqs(1, "127.0.0.1:9001", 1, 1, list, 2);

//        RaftRpcReq(type=2, data=AppendEntriesReqs(term=0, leaderId=127.0.0.19001, prevLogIndex=0, prevLogTerm=0, entries=[LogEntry(index=1, term=20, command=LogEntry.Command(key=key, value=value))], leaderCommit=0))
//        AppendEntriesResp resp = consensus.appendEntries(reqs);
//        RaftRpcClient.getClient().shutdown();
    }

    @Test
    public void initTask(){

        RaftThreadPoolExecutor raftThreadPoolExecutor;
        //todo 优化参数
        raftThreadPoolExecutor = new RaftThreadPoolExecutor(10, 10, 600,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());

        ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(1);
        ScheduledThreadPoolExecutor timeOutExecutor = new ScheduledThreadPoolExecutor(1);
        Set<main.entity.Peer> peerSet = new HashSet<>();
        peerSet.add(new Peer("127.0.0.1:9001"));
//        peerSet.add(new Peer("127.0.0.1:9002"));
//        peerSet.add(new Peer("127.0.0.1:9003"));


    }



    @Test
    public void heartBeatResult(){
            Peer p = new Peer("127.0.0.1:9001");
            AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();
            Metadata.currentTerm = 10002;
            appendEntriesReqs.setTerm(Metadata.currentTerm);
            appendEntriesReqs.setLeaderId(null);

            appendEntriesReqs.setLeaderCommit(Metadata.commitIndex);

            RaftRpcReq req = new RaftRpcReq(CommandType.appendLog.getType(), appendEntriesReqs);
            try {
                AppendEntriesResp appendEntriesResp = (AppendEntriesResp) RaftRpcClient.getClient().invokeSync(p.getAddr(), req, 3000);
                if(appendEntriesResp.getSuccess()){

//                    reentrantLock.lock();
//                    long currentTime = System.currentTimeMillis();
//                    lastHeartBeatTime = Math.max(currentTime,lastHeartBeatTime);
//                    reentrantLock.unlock();


                }
            } catch (Exception e) {
                log.error("心跳远程调用error", e);
            }


        };

//        RaftRpcReq(type=2, data=AppendEntriesReqs(term=1000000, leaderId=null, prevLogIndex=0, prevLogTerm=0, entries=null, leaderCommit=0))

    @Test
    public void kevWriteTest() {
        Peer p = new Peer("127.0.0.1:9002");

        KVReqs kvReqs = new KVReqs("test","value");


        KVResp kvResp = null;
        RaftRpcReq  raftWriteRpcReq = new RaftRpcReq(CommandType.addKv.getType(),kvReqs);
        RaftRpcReq  raftReadRpcReq = new RaftRpcReq(CommandType.readKv.getType(),kvReqs);

        try {
            kvResp = (KVResp) RaftRpcClient.getClient().invokeSync(p.getAddr(), raftWriteRpcReq, 3000);
            log.info(kvResp.getMsg());
            kvResp = (KVResp) RaftRpcClient.getClient().invokeSync(p.getAddr(), raftReadRpcReq, 3000);
            log.info(kvResp.getMsg());
        } catch (InterruptedException | RemotingException e) {
            e.printStackTrace();
        }

    }

}
