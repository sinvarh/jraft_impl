package main;

import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.RpcServer;
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
import main.rpc.RaftRpcServer;
import main.rpc.RaftServerUsersProcessor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static main.config.NodeStatus.FOLLOWER;
import static main.config.NodeStatus.LEADER;

public class NodeImpl implements Node {
    /**
     * 服务器已知最新的任期
     */
    public int currentTerm;

    /**
     *votedFor	当前任期内收到选票的 candidateId，如果没有投给任何候选人 则为空
     */
    public String voteFor;

    public LogModule logModule;

    public StateMachine stateMachine;

    public Consensus consensus;

    /** 已知的最大的已经被提交的日志条目的索引值 */
    public long commitIndex;

    /** 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增) */
    public long lastApplied = 0;

    public int status = NodeStatus.FOLLOWER;

    /**
     * 每个节点的地址
     */
    public Set<main.entity.Peer> peerSet;
    /**
     * 成为了领导人需要用的的值
     */
    /** 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一） */
    public Map<main.entity.Peer, Long> nextIndexMap;

    /** 对于每一个服务器，已经复制给他的日志的最高索引值 */
    public Map<main.entity.Peer, Long> matchIndexMap;


    /**
     * rpc client
     */
    public RpcClient rpcClient;

    public RpcServer rpcServer;


    public RaftThreadPoolExecutor raftThreadPoolExecutor;
    public void init(){

    }

    void  initRpcClient(){
        // 1. create a rpc client
        rpcClient = new RpcClient();
        // 2. add processor for connect and close event if you need
        RaftServerUsersProcessor raftServerUsersProcessor = new RaftServerUsersProcessor();
        rpcClient.registerUserProcessor(raftServerUsersProcessor);
        // 3. do init
        rpcClient.startup();
    }

    public static void main(String[] args) {
        new RaftRpcServer();
        System.out.println("fuck u ");
        System.out.println("fuc u 2");
    }

    /**
     *   处理投票请求
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
     * @param reqs
     * @return
     */
    @Override
    public KVResp handleClientRequest(KVReqs reqs) {

        if(status!=LEADER){
            return "";
        }
        //同步到所有的节点上
        int quorum = 0;
        for(main.entity.Peer peer:peerSet){
            //获取同步结果

        }
        raftThreadPoolExecutor.invokeAll()
        //如果同步了大多数
        if(){
        }else {
            //回滚
        }
        return null;
    }

    public Future<Boolean> appendEntriesRpc(main.entity.Peer peer, LogEntry logEntry ){

        AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();

        return null;
    }

    //
    public Callable<Boolean> replicateResult(main.entity.Peer p, List<LogEntry> entries){
        return () -> {
            RaftRpcReq req = new RaftRpcReq(2, "hello world sync");
            AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();
            appendEntriesReqs.setEntries(entries);
            appendEntriesReqs.setLeaderId();
            appendEntriesReqs.setLeaderCommit();
            //如果是第一次，沒有perIndex
            appendEntriesReqs.setPrevLogIndex();
            appendEntriesReqs.setPrevLogTerm();


            //构造函数
            AppendEntriesResp appendEntriesResp =  rpcClient.invokeSync(p.getAddr(), req, 3000);
            if(appendEntriesResp.getTerm()>currentTerm){
                //变成follower
                status = FOLLOWER;
                currentTerm = appendEntriesReqs.getTerm();
                return false;
            }

            //if success
            if(appendEntriesResp.getSuccess()){
                return true;
            }else{
                //失败了，减少index 重试,循环重试
                nextIndexMap.put();
                matchIndexMap.put()
                entries.add(0,);
            }
        }
    };



}

