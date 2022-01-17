package main;

import main.Node;
import main.entity.*;
import main.rpc.RaftRpcServer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static main.entity.NodeStatus.LEADER;

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
    public Set<Peer> peerSet;
    /**
     * 成为了领导人需要用的的值
     */
    /** 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一） */
    public Map<Peer, Long> nextIndexMap;

    /** 对于每一个服务器，已经复制给他的日志的最高索引值 */
    public Map<Peer, Long> matchIndexMap;

    public void init(){

    }

    public static void main(String[] args) {
        new RaftRpcServer();
        System.out.println("fuck u ");
        System.out.println("fuc u 2");
    }

    /**
     *   处理投票请求
     */
    public RequestVoteResp handlerRequestVote(RequestVoteReqs reqs) {
        return consensus.requestVote(reqs);
    }

    /**
     * 处理append请求
     */

    public AppendEntriesResp handleAppendEntries(AppendEntriesReqs reqs) {
        return consensus.appendEntries(reqs);
    }

    /**
     * 处理客户端请求
     * @param reqs
     * @return
     */
    public KVResp handleClientRequest(KVReqs reqs) {

        if(status!=LEADER){
            return "";
        }
        //同步到所有的节点上
        int quorum = 0;
        for(Peer peer:peerSet){
            //获取同步结果
            CompletableFuture<Boolean> completableFuture = CompletableFuture.completedFuture()
        }
        //如果同步了大多数
        if(){

        }else {
            //回滚
        }
        return null;
    }

    public Future<Boolean> appendEntriesRpc(Peer peer,LogEntry logEntry ){

        AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();

        return null;
    }

}

