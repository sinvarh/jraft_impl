package main;

import main.entity.*;

public class ConsensusImpl implements Consensus{
    private NodeImpl node;

    public ConsensusImpl(NodeImpl node) {
        this.node = node;
    }

    /**
     * 如果term < currentTerm返回 false （5.2 节）
     * 如果 votedFor 为空或者为 candidateId，并且候选人的日志至少和自己一样新，那么就投票给他
     * @param reqs
     * @return
     */
    @Override
    public RequestVoteResp requestVote(RequestVoteReqs reqs) {
        if(reqs.getTerm()<node.currentTerm){
            return  RequestVoteResp.builder().term(reqs.getTerm()).voteGranted(false).build();
        }
        if(node.voteFor==null||node.voteFor.equals(reqs.getCandidateId())){

            if (node.logModule.getLast() != null) {
                // 对方没有自己新
                if (node.logModule.getLast().getTerm() > reqs.getLastLogTerm()) {
                    return RequestVoteResp.builder().term(reqs.getTerm()).voteGranted(false).build();
                }
                // 对方没有自己新
                if (node.logModule.getLastIndex() > reqs.getLastLogIndex()) {
                    return RequestVoteResp.builder().term(reqs.getTerm()).voteGranted(false).build();
                }
            }
        }

        // 切换状态
        node.status = NodeStatus.FOLLOWER;
        // 更新
//        node.peerSet.setLeader(new Peer(reqs.getCandidateId()));
        node.currentTerm = reqs.getTerm();
        node.voteFor = reqs.getCandidateId();

        return  RequestVoteResp.builder().term(reqs.getTerm()).voteGranted(true).build();
    }

    /**
     *
     * @param reqs
     * @return
     *
     * 返回假 如果领导人的任期小于接收者的当前任期（译者注：这里的接收者是指跟随者或者候选人）（5.1 节）
     * 返回假 如果接收者日志中没有包含这样一个条目 即该条目的任期在 prevLogIndex 上能和 prevLogTerm 匹配上
     * （译者注：在接收者日志中 如果能找到一个和 prevLogIndex 以及 prevLogTerm 一样的索引和任期的日志条目 则继续执行下面的步骤 否则返回假）（5.3 节）
     * 如果一个已经存在的条目和新条目（译者注：即刚刚接收到的日志条目）发生了冲突（因为索引相同，任期不同），那么就删除这个已经存在的条目以及它之后的所有条目 （5.3 节）
     * 追加日志中尚未存在的任何新条目
     * 如果领导人的已知已提交的最高日志条目的索引大于接收者的已知已提交最高日志条目的索引（leaderCommit > commitIndex），则把接收者的已知已经提交的最高的日志条目的索引commitIndex 重置为 领导人的已知已经提交的最高的日志条目的索引 leaderCommit 或者是 上一个新条目的索引 取两者的最小值
     */
    @Override
    public AppendEntriesResp appendEntries(AppendEntriesReqs reqs) {
        AppendEntriesResp res = AppendEntriesResp.builder().term(reqs.getTerm()).success(false).build();

        if(reqs.getTerm()<node.currentTerm){
            return res;
        }


        if(reqs.getTerm()>node.currentTerm){
            node.status =NodeStatus.FOLLOWER;
        }

        node.currentTerm =reqs.getTerm();

        //心跳
        if(reqs.getEntries()==null||reqs.getEntries().isEmpty()){
            res.setSuccess(true);
            return res;
        }
        //不是心跳,非第一次
        if(node.logModule.getLastIndex()!=0&&reqs.getPrevLogIndex()!=0){
            LogEntry logEntry = node.logModule.read(reqs.getPrevLogIndex());
            if(logEntry!=null){
                // 如果日志在 prevLogIndex 位置处的日志条目的任期号和 prevLogTerm 不匹配，则返回 false
                // 需要减小 nextIndex 重试.
                if(logEntry.getTerm()!=reqs.getPrevLogTerm()){
                    return res;
                }
            }else {
                //index 不对，递减nextIndex
                return res;
            }
        }

        // 这里比较关键
        // 如果已经存在的日志条目和新的产生冲突（索引值相同但是任期号不同），删除这一条和之后所有的
        LogEntry exitEntry = node.logModule.read(reqs.getPrevLogIndex()+1);
        if(exitEntry!=null && exitEntry.getTerm()!= reqs.getEntries().get(0).getTerm()){
            //删除这一条之后所有的
            node.logModule.removeFromStartIndex(reqs.getPrevLogIndex()+1);
        }else if(exitEntry!=null){
            //日志已存在
            res.setSuccess(true);
            return res;
        }

        //写入到状态机中，同步写入
        for (LogEntry logEntry:reqs.getEntries()) {
            node.logModule.write(logEntry);
            node.stateMachine.apply(logEntry);
            res.setSuccess(true);
        }

        //如果 leaderCommit > commitIndex，令 commitIndex 等于 leaderCommit 和 新日志条目索引值中较小的一个
        if(reqs.getLeaderCommit()>node.commitIndex){
            int commitIndex = (int) Math.min(reqs.getLeaderCommit(),node.logModule.getLastIndex());
            node.commitIndex = commitIndex;
            node.lastApplied = commitIndex;
        }

        return res;
    }
}
