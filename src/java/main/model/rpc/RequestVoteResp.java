package main.model.rpc;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 选举的结果
 *
 * @author sinvar
 */
@Data
@Builder
public class RequestVoteResp implements Serializable {
    private static final long serialVersionUID = 6225157743423405896L;
    /**
     * 任期
     */
    private int term;

    /**
     * 候选人赢得了此张选票时为真
     */
    private Boolean voteGranted;
}
