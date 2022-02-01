package main;

import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.RpcServer;
import main.config.Metadata;
import main.config.NodeStatus;
import main.config.RaftThreadPoolExecutor;
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

import java.util.*;
import java.util.concurrent.*;

import static main.config.NodeStatus.FOLLOWER;
import static main.config.NodeStatus.LEADER;

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
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    public Map<Peer, Long> matchIndexMap;


    /**
     * rpc client
     */
    public RpcClient raftRpcClient;

    public RaftRpcServer raftRpcServer;


    public RaftThreadPoolExecutor raftThreadPoolExecutor;

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
    }


    public static void main(String[] args) {
        System.out.println("fuck u ");
        System.out.println("fuc u 2");
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
        //同步到所有的节点上
        int quorum = 0;
        List<Callable<Boolean>> callableList = new ArrayList<>(peerSet.size());
        for (Peer peer : peerSet) {
            // todo index term
            callableList.add(replicateResult(peer, new LogEntry(1L, 1,
                    new LogEntry.Command(reqs.getKey(), reqs.getValue()))));
        }
        try {
            List<Future<Boolean>> resList = raftThreadPoolExecutor.invokeAll(callableList);
            for (Future<Boolean> res : resList) {
                if (res.get()) {
                    quorum++;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //如果同步了大多数
        if (quorum > peerSet.size() / 2) {

        } else {
            //回滚
        }
        return null;
    }

    public Future<Boolean> appendEntriesRpc(Peer peer, LogEntry logEntry) {

        AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();

        return null;
    }

    //
    public Callable<Boolean> replicateResult(Peer p, LogEntry logEntry) {
        return () -> {
            RaftRpcReq req = new RaftRpcReq(2, "hello world sync");
            AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();
            appendEntriesReqs.setEntries(Collections.singletonList(logEntry));
            appendEntriesReqs.setLeaderId();
            appendEntriesReqs.setLeaderCommit(Metadata.commitIndex);
            //如果是第一次，perIndex是0
            appendEntriesReqs.setPrevLogIndex(logModule.);
            appendEntriesReqs.setPrevLogTerm(logModule.ge);


            //构造函数
            AppendEntriesResp appendEntriesResp = (AppendEntriesResp) raftRpcClient.invokeSync(p.getAddr(), req, 3000);
            if (appendEntriesResp.getTerm() > Metadata.currentTerm) {
                //变成follower
                status = FOLLOWER;
                Metadata.currentTerm = appendEntriesReqs.getTerm();
                return false;
            }

            //if success
            if (appendEntriesResp.getSuccess()) {
                return true;
            } else {
                //失败了，减少index 重试,循环重试
                nextIndexMap.put();
                matchIndexMap.put();
            }
        }
    }

    ;


}

