package main;

import main.Node;
import main.entity.NodeStatus;
import main.entity.Peer;

import java.util.Map;

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

    /** 已知的最大的已经被提交的日志条目的索引值 */
    public long commitIndex;

    /** 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增) */
    public long lastApplied = 0;

    /** 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一） */
    public Map<Peer, Long> nextIndexMap;

    /** 对于每一个服务器，已经复制给他的日志的最高索引值 */
    public Map<Peer, Long> matchIndexMap;

    public int status = NodeStatus.FOLLOWER;


}
