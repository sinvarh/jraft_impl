package main.rpc;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import main.model.rpc.common.RaftRpcReq;

/**
 * a demo user processor for rpc server
 *
 * @author xiaomin.cxm
 * @version $Id: SimpleServerUserProcessor.java, v 0.1 Jan 7, 2016 3:01:49 PM xiaomin.cxm Exp $
 */
public class RaftServerUsersProcessor extends SyncUserProcessor<RaftRpcReq> {



    @Override
    public Object handleRequest(BizContext bizCtx, RaftRpcReq request) throws Exception {
        System.out.println(request.getData());
        return "command type is "+request.getData().toString();
    }

    @Override
    public String interest() {
        return RaftRpcReq.class.getName();
    }


}