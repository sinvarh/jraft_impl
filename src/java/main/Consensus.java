package main;

import main.entity.AppendEntriesReqs;
import main.entity.AppendEntriesResp;
import main.entity.RequestVoteReqs;
import main.entity.RequestVoteResp;

/**
 * raft 发起的请求
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
