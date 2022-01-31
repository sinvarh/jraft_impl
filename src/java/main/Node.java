package main;

import main.model.app.KVReqs;
import main.model.app.KVResp;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.AppendEntriesResp;
import main.model.rpc.RequestVoteReqs;
import main.model.rpc.RequestVoteResp;

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
