package main.model.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * 选举的请求
 * @author sinvar
 */
@Data
public class RequestVoteReqs implements Serializable {
    private static final long serialVersionUID = -7132772098280045823L;
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
