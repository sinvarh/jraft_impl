package main;

import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.RpcServer;
import lombok.extern.slf4j.Slf4j;
import main.config.Metadata;
import main.config.NodeStatus;
import main.config.RaftThreadPoolExecutor;
import main.constant.CommandType;
import main.constant.Constants;
import main.model.app.KVReqs;
import main.model.app.KVResp;
import main.model.log.LogEntry;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.AppendEntriesResp;
import main.model.rpc.RequestVoteReqs;
import main.model.rpc.RequestVoteResp;
import main.model.rpc.common.RaftRpcReq;
import main.rpc.RaftRpcClient;
import main.rpc.RaftRpcServer;
import main.rpc.RaftServerUsersProcessor;
import main.entity.Peer;
import org.apache.log4j.BasicConfigurator;

import java.time.Period;
import java.util.*;
import java.util.concurrent.*;

import static main.config.NodeStatus.FOLLOWER;
import static main.config.NodeStatus.LEADER;

/**
 * @author sinvar
 */
@Slf4j
public class NodeImpl implements Node {

    public LogModule logModule;

    public StateMachine stateMachine;

    public Consensus consensus;

    /**
     * 已知的最大的已经被提交的日志条目的索引值
     */
    public long commitIndex;

    /**
     * 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增)
     */
    public long lastApplied = 0;

    public int status = NodeStatus.FOLLOWER;

    /**
     * 每个节点的地址
     */
    public Set<Peer> peerSet;
    /**
     * 成为了领导人需要用的的值
     */
    /**
     * 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    public Map<Peer, Long> nextIndexMap;

    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值。用来更新commitIndex
     */
    public Map<Peer, Long> matchIndexMap;


    /**
     * rpc client
     */
    public RpcClient raftRpcClient;

    public RaftRpcServer raftRpcServer;


    public RaftThreadPoolExecutor raftThreadPoolExecutor;

    //todo 重试队列
    public LinkedBlockingQueue<String> linkedBlockingQueue;

    public void init(int port) {
        logModule = new DefaultLogModule(port);
        stateMachine = new StateMachineImpl(port);
        consensus = new ConsensusImpl(logModule, stateMachine);

        //rpc 模块初始化
        raftRpcServer = new RaftRpcServer(port, consensus);
        raftRpcClient = RaftRpcClient.getClient();

        //todo 优化参数
        raftThreadPoolExecutor = new RaftThreadPoolExecutor(10, 10, 600,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());

        ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(3);
        ScheduledThreadPoolExecutor timeOutExecutor = new ScheduledThreadPoolExecutor(3);

        Peer peer1 = new Peer("127.0.0.1:9902");
        Peer peer2 = new Peer("127.0.0.1:9903");
        Peer peer3 = new Peer("127.0.0.1:9903");
        peerSet  = new HashSet<>();
        peerSet.add(peer1);
        peerSet.add(peer2);
        peerSet.add(peer3);

        heartbeatExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<Callable<Boolean>> callableList = new ArrayList<>(peerSet.size());
                for (Peer peer : peerSet) {
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
        },1000,1000,TimeUnit.MICROSECONDS);
        timeOutExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                log.info("timeout");
                List<Callable<Boolean>> callableList = new ArrayList<>(peerSet.size());
                for (Peer peer : peerSet) {
                    callableList.add(voteResult(peer));
                }
                try {
                    List<Future<Boolean>> resList = raftThreadPoolExecutor.invokeAll(callableList);
                    for(Future<Boolean> heartBeatRes :resList){
                        if(!heartBeatRes.get()){
                            //todo 重试
                            log.error("rpc res false error");
                        };
                    }
                } catch (Exception e) {
                    log.error("heart invoke all error");
                }
            }
        },0,1,TimeUnit.SECONDS);
    }


    public static void main(String[] args) {
        BasicConfigurator.configure();
        NodeImpl node = new NodeImpl();
        node.init(9901);

    }

    /**
     * 处理投票请求
     */
    @Override
    public RequestVoteResp handlerRequestVote(RequestVoteReqs reqs) {
        return consensus.requestVote(reqs);
    }

    /**
     * 处理append请求
     */

    @Override
    public AppendEntriesResp handleAppendEntries(AppendEntriesReqs reqs) {
        return consensus.appendEntries(reqs);
    }

    /**
     * 处理客户端请求
     *
     * @param reqs
     * @return
     */
    @Override
    public KVResp handleClientRequest(KVReqs reqs) {

        if (status != LEADER) {
            return new KVResp(-1, "not leader");
        }

        LogEntry logEntry = new LogEntry(Metadata.commitIndex + 1, Metadata.currentTerm,
                new LogEntry.Command(reqs.getKey(), reqs.getValue()));
        //写入log
        logModule.write(logEntry);

        //同步到所有的节点上
        int quorum = 0;
        List<Callable<Boolean>> callableList = new ArrayList<>(peerSet.size());
        for (Peer peer : peerSet) {
            //todo 排除自己
//            long nextIndex = nextIndexMap.get(peer);
//            LogEntry logEntry = logModule.read(nextIndex);
            callableList.add(replicateResult(peer, logEntry));
        }
        try {
            //线程池invoke all,todo// 这里面实现也是循环等待
            List<Future<Boolean>> resList = raftThreadPoolExecutor.invokeAll(callableList);
            for (Future<Boolean> res : resList) {
                if (res.get()) {
                    quorum++;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //upgrade commitIndex;
        updateCommitIndex();

        //如果同步了大多数
        if (quorum > peerSet.size() / 2) {
            //应用到状态机上
            stateMachine.apply(logEntry);
            commitIndex = logEntry.getIndex();
        } else {
            //回滚
            logModule.removeFromStartIndex(logEntry.getIndex());
            return new KVResp(-1, "没有获得半数同意");
        }
        return new KVResp(0, "success");
    }


    // 复制到其他机器上
    private Callable<Boolean> replicateResult(Peer p, LogEntry logEntry) {
        return () -> {

            AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();
            appendEntriesReqs.setEntries(Collections.singletonList(logEntry));
            appendEntriesReqs.setLeaderId(Metadata.leaderAddr);
            appendEntriesReqs.setLeaderCommit(Metadata.commitIndex);
            //如果是第一次，perIndex是0
            LogEntry preLogEntry = logModule.read(logEntry.getIndex() - 1);
            if (preLogEntry != null) {
                appendEntriesReqs.setPrevLogIndex(preLogEntry.getIndex());
                appendEntriesReqs.setPrevLogTerm(preLogEntry.getTerm());
            } else {
                appendEntriesReqs.setPrevLogIndex(0);
            }

            RaftRpcReq req = new RaftRpcReq(CommandType.appendLog.getType(), appendEntriesReqs);
            //构造函数
            AppendEntriesResp appendEntriesResp = (AppendEntriesResp) raftRpcClient.invokeSync(p.getAddr(), req, 3000);
            if (appendEntriesResp.getTerm() > Metadata.currentTerm) {
                //变成follower
                status = FOLLOWER;
                Metadata.currentTerm = appendEntriesResp.getTerm();
                return false;
            }

            //if success
            if (appendEntriesResp.getSuccess()) {
                nextIndexMap.put(p, logEntry.getIndex() + 1);
                matchIndexMap.put(p, logEntry.getIndex());
                return true;
            } else {
                //失败了，减少index 重试,循环重试
                if (logEntry.getIndex() - 1 > 0) {
                    nextIndexMap.put(p, logEntry.getIndex() - 1);
                } else {
                    nextIndexMap.put(p, 1L);
                }
                return false;
            }

        };
    }


    private Callable<Boolean> heartBeatResult(Peer p){
        return () -> {
            if(Metadata.status== NodeStatus.LEADER) {
                AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();
                appendEntriesReqs.setTerm(Metadata.currentTerm);
                appendEntriesReqs.setLeaderId(Metadata.leaderAddr);
                appendEntriesReqs.setLeaderCommit(Metadata.commitIndex);

                RaftRpcReq req = new RaftRpcReq(CommandType.appendLog.getType(), appendEntriesReqs);
                try {
                    AppendEntriesResp appendEntriesResp = (AppendEntriesResp) raftRpcClient.invokeSync(p.getAddr(), req, 3000);
                    if(appendEntriesResp.getSuccess()){
                        return true;
                    }
                } catch (Exception e) {
                    log.error("心跳远程调用error", e);
                }
            }else{
                log.info("not leader");
            }
            return false;
        };
    }

    private Callable<Boolean> voteResult(Peer p){
        return () -> {
            if(Metadata.status==NodeStatus.FOLLOWER){
                RequestVoteReqs requestVoteReqs = new RequestVoteReqs();
                requestVoteReqs.setTerm(Metadata.currentTerm+1);
                requestVoteReqs.setCandidateId(Metadata.addr);
                requestVoteReqs.setLastLogIndex(Metadata.commitIndex);

                LogEntry logEntry = logModule.read(Metadata.commitIndex);
                if(logEntry!=null) {
                    requestVoteReqs.setLastLogTerm(logEntry.getTerm());
                }
                RaftRpcReq req = new RaftRpcReq(CommandType.vote.getType(), requestVoteReqs);

                try {
                    RequestVoteResp voteResp = (RequestVoteResp) raftRpcClient.invokeSync(p.getAddr(), req, 3000);
                    if(voteResp.getVoteGranted()){
                        return true;
                    }
                } catch (Exception e) {
                    log.error("心跳远程调用error", e);
                }
            }
            return false;
        };

    }

    //upgrade commitIndex
    void updateCommitIndex() {
        ArrayList<Long> list = new ArrayList<>(matchIndexMap.values());
        Collections.sort(list);
        Long index = list.get(list.size() / 2);
        if (index > 0) {
            LogEntry logEntry = logModule.read(index);
            if (logEntry != null) {
                if (logEntry.getTerm() == Metadata.currentTerm) {
                    commitIndex = index;
                }
            }
        }
    }

}

