package main.rpc;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import main.Consensus;
import main.DefaultLogModule;
import main.Node;
import main.constant.CommandType;
import main.constant.Constants;
import main.model.app.KVReqs;
import main.model.app.KVResp;
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

    private Node node;

    public RaftServerUsersProcessor (Node node) {

        this.node = node;
    }

    @Override
    public Object handleRequest(BizContext bizCtx, RaftRpcReq request) throws Exception {
        System.out.println(request.getData());
        //todo if else 的优化
        if (request.getType() == CommandType.vote.getType()) {
            return node.handlerRequestVote((RequestVoteReqs) request.getData());
        }
        if (request.getType() == CommandType.appendLog.getType()) {
            return node.handleAppendEntries((AppendEntriesReqs) request.getData());
        }
        if(request.getType() == CommandType.addKv.getType()){
            return node.handleClientWriteRequest((KVReqs) request.getData());
        }
        if(request.getType() == CommandType.readKv.getType()){
            return node.handleClientReadRequest((KVReqs) request.getData());
        }
        return new RaftRpcResp(Constants.commandNotFind, null);
    }

    @Override
    public String interest() {
        return RaftRpcReq.class.getName();
    }


}