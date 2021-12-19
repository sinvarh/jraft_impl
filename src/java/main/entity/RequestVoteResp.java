package main.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 选举的结果
 */
@Data
@Builder
public class RequestVoteResp {
    /**
     * 任期
     */
    private int term;

    /**
     * 候选人赢得了此张选票时为真
     */
    private Boolean voteGranted;
}
