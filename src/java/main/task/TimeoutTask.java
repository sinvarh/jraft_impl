package main.task;


import com.alipay.remoting.rpc.RpcClient;
import lombok.extern.slf4j.Slf4j;
import main.LogModule;
import main.config.Metadata;
import main.config.NodeStatus;
import main.constant.CommandType;
import main.entity.Peer;
import main.model.log.LogEntry;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.RequestVoteReqs;
import main.model.rpc.common.RaftRpcReq;

/**
 * timeoutTask
 */
@Slf4j
public class TimeoutTask implements Runnable{

    private final Peer peer;
    private final RpcClient raftRpcClient;
    private final LogModule logModule;

    public TimeoutTask(Peer peer, RpcClient raftRpcClient,LogModule logModule) {
        this.peer = peer;
        this.raftRpcClient = raftRpcClient;
        this.logModule = logModule;
    }

    @Override
    public void run() {
        if(Metadata.status==NodeStatus.FOLLOWER){
            Metadata.status = NodeStatus.CANDIDATE;

        }else {
            log.info("not leader");
        }
    }
}
