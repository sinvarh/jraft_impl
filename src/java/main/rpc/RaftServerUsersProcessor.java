package main.rpc;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.NamedThreadFactory;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * a demo user processor for rpc server
 *
 * @author xiaomin.cxm
 * @version $Id: SimpleServerUserProcessor.java, v 0.1 Jan 7, 2016 3:01:49 PM xiaomin.cxm Exp $
 */
public class RaftServerUsersProcessor extends SyncUserProcessor<RaftRpcRequest> {



    @Override
    public Object handleRequest(BizContext bizCtx, RaftRpcRequest request) throws Exception {
        System.out.println(request.getCommandType());
        return "command type is "+request.getCommandType();
    }

    @Override
    public String interest() {
        return RaftRpcRequest.class.getName();
    }


}