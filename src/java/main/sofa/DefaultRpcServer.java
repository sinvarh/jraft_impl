package main.sofa;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.RpcServer;
import main.entity.RpcReqs;
import main.entity.RpcResp;

/**
 * @program: jraft
 * @description:
 * @author: hxh E-mail:hxh@100cb.cn
 * @create: 2022-01-14 00:34
 */
public class DefaultRpcServer implements RaftRpcServer{

    private RpcServer rpcServer;
    private boolean flag;

    @Override
    public void start(int port ) {
        if (flag) {
            return;
        }
        synchronized (this) {
            if (flag) {
                return;
            }

            rpcServer = new RpcServer(port, false, false);

            rpcServer.registerUserProcessor(new RaftUserProcessor<RpcResp>() {
                @Override
                public Object handleRequest(BizContext bizContext, RpcResp rpcResp) throws Exception {
                    return null;
                }
            });

            flag = true;
        }
        rpcServer.start();
    }

    @Override
    public void stop() {
        rpcServer.start();
    }

    @Override
    public void handleVote() {

    }

    @Override
    public void handleAppendLog() {

    }
}
