package main;

import main.entity.*;

/**
 * node interface
 */
public interface Node {

    /**
     * 处理投票请求
     */
    RequestVoteResp handlerRequestVote(RequestVoteReqs reqs);

    /**
     * 处理添加日志请求
     */
    AppendEntriesResp handleAppendEntries(AppendEntriesReqs reqs);

    /**
     * 客户端请求
     */
    KVResp handleClientRequest(KVReqs reqs);
}
