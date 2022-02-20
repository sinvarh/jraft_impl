package main.config;
/**
 * @program: jraft
 * @description: 存放状态机相关的数据
 * @author: hxh E-mail:hxh@100cb.cn
 * @create: 2022-01-31 17:41
 */

public class Metadata {
    /**
     * 服务器已知最新的任期
     */
    public static int currentTerm;

    /**
     * votedFor	当前任期内收到选票的 candidateId，如果没有投给任何候选人 则为空
     */
    public static volatile String voteFor;

    /**
     * 已知的最大的已经被提交的日志条目的索引值，
     * 啥时候更新这个值，更新这个值的时间点
     */
    public static long commitIndex;

    /**
     * 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增)
     */
    public static long lastApplied = 0;

    /**
     * 当前状态
     */
    public static volatile int status = NodeStatus.FOLLOWER;

    /**
     *自己的地址
     */
    public static volatile String addr;

    /**
     * 上次心跳的时间
     */
    public static   volatile long lastHeartBeatTime = 0;
}
