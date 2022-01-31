package main.rpc;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import main.Consensus;
import main.DefaultLogModule;
import main.constant.CommandType;
import main.constant.Constants;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.RequestVoteReqs;
import main.model.rpc.common.RaftRpcReq;
import main.model.rpc.common.RaftRpcResp;

/**
 * a demo user processor for rpc server
 *
 * @author xiaomin.cxm
 * @version $Id: SimpleServerUserProcessor.java, v 0.1 Jan 7, 2016 3:01:49 PM xiaomin.cxm Exp $
 */
public class RaftServerUsersProcessor extends SyncUserProcessor<RaftRpcReq> {

    private Consensus consensus;

    public RaftServerUsersProcessor(Consensus consensus) {
        this.consensus = consensus;
    }

    @Override
    public Object handleRequest(BizContext bizCtx, RaftRpcReq request) throws Exception {
        System.out.println(request.getData());
        //todo if else 的优化
        if (request.getType() == CommandType.vote.getType()) {
            consensus.requestVote((RequestVoteReqs) request.getData());
        }
        if (request.getType() == CommandType.appendLog.getType()) {
            consensus.appendEntries((AppendEntriesReqs) request.getData());
        }
        return new RaftRpcResp(Constants.commandNotFind, null);
    }

    @Override
    public String interest() {
        return RaftRpcReq.class.getName();
    }


}