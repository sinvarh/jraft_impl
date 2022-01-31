package main;

import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.AppendEntriesResp;
import main.model.rpc.RequestVoteReqs;
import main.model.rpc.RequestVoteResp;

/**
 * raft 发起的请求
 * @author sinvar
 */
public interface Consensus {
    /**
     * 投票request
     */
    RequestVoteResp requestVote(RequestVoteReqs reqs);

    /**
     * 追加日志request
     * @param
     * @return
     */
   AppendEntriesResp appendEntries(AppendEntriesReqs reqs);
}
