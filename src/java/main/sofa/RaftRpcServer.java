package main.sofa;

/**
 * @program: jraft
 * @description:
 * @author: hxh E-mail:hxh@100cb.cn
 * @create: 2022-01-14 00:28
 */
public interface RaftRpcServer {
    /**
     * 开始
     */
    void start(int port);

    /**
     * 结束
     */
    void stop();

    /**
     * 处理投票请求
     */
    void handleVote();

    /**
     * 处理日志请求
     */
    void handleAppendLog();
}
