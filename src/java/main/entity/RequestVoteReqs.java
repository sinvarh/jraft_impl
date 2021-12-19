package main.entity;

import lombok.Data;

/**
 * 选举的请求
 */
@Data
public class RequestVoteReqs {
    /**
     * 任期
     */
    private int term;
    /**
     * 候选人的 ID
     */
    private String candidateId;

    /**
     * 候选人的最后日志条目的索引值
     */
    private long lastLogIndex;

    /**
     * 候选人最后日志条目的任期号
     */
    private long lastLogTerm;

}
