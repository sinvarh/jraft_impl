package main.task;


import com.alipay.remoting.rpc.RpcClient;
import lombok.extern.slf4j.Slf4j;
import main.config.Metadata;
import main.config.NodeStatus;
import main.constant.CommandType;
import main.entity.Peer;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.common.RaftRpcReq;

/**
 * 心跳task
 */
@Slf4j
public class HeartBeatTask implements Runnable{

    private final Peer peer;
    private final RpcClient raftRpcClient;

    public HeartBeatTask(Peer peer, RpcClient raftRpcClient) {
        this.peer = peer;
        this.raftRpcClient = raftRpcClient;
    }

    @Override
    public void run() {
        if(Metadata.status== NodeStatus.LEADER) {
            AppendEntriesReqs appendEntriesReqs = new AppendEntriesReqs();
            appendEntriesReqs.setTerm(Metadata.currentTerm);
            appendEntriesReqs.setLeaderId(Metadata.leaderAddr);
            appendEntriesReqs.setLeaderCommit(Metadata.commitIndex);

            RaftRpcReq req = new RaftRpcReq(CommandType.appendLog.getType(), appendEntriesReqs);
            try {
                raftRpcClient.invokeSync(peer.getAddr(), req, 3000);
            } catch (Exception e) {
                log.error("心跳远程调用error", e);
            }
        }else{
            log.info("not leader");
        }
    }
}
