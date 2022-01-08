package main;

import main.entity.AppendEntriesReqs;
import main.entity.AppendEntriesResp;
import main.entity.RequestVoteReqs;
import main.entity.RequestVoteResp;

/**
 * node interface
 */
public interface Node {

   //处理投票请求
   RequestVoteResp handlerRequestVote(RequestVoteReqs reqs);

   //处理添加日志请求
   AppendEntriesResp handleAppendEntries(AppendEntriesReqs reqs);

//   handlerClientRequest();
}
