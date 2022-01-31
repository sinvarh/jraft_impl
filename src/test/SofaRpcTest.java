import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import main.*;
import main.constant.CommandType;
import main.model.log.LogEntry;
import main.model.rpc.AppendEntriesReqs;
import main.model.rpc.common.RaftRpcReq;
import main.rpc.RaftRpcClient;
import main.rpc.RaftRpcServer;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

@Slf4j
public class SofaRpcTest {

    private RaftRpcServer raftRpcServer;
    private Consensus consensus;
    private LogModule logModule;
    private StateMachine stateMachine;
    private int serverPort;

    @Before
    public void setUp() {
        serverPort = 9001;
        logModule = new DefaultLogModule(serverPort);
        stateMachine = new StateMachineImpl(serverPort);
        consensus = new ConsensusImpl(logModule, stateMachine);
        raftRpcServer = new RaftRpcServer(serverPort, consensus);


    }

    @Test
    public void testAppendLog() throws RemotingException, InterruptedException {
        String addr = "127.0.0.1:" + serverPort;
        List<LogEntry> list = Collections.singletonList(new LogEntry(1L, 1, new LogEntry.Command("1111", "2222")));
        AppendEntriesReqs reqs = new AppendEntriesReqs(1, "127.0.0.1:9001", 0, 1, list, 1);

        RaftRpcReq req = new RaftRpcReq(CommandType.appendLog.getType(), reqs);
        RaftRpcClient.getClient().invokeSync(addr, req, 3000);
    }


}
