package main.model.rpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.log.LogEntry;

import java.util.List;

/**
 * 追加日志请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppendEntriesReqs {
    /**
     * 任期
     */
    private int term;
    /**
     * 领导人 ID 因此跟随者可以对客户端进行重定向（译者注：跟随者根据领导人 ID 把客户端的请求重定向到领导人，比如有时客户端把请求发给了跟随者而不是领导人）
     */
    private String leaderId;

    /**
     * 紧邻新日志条目之前的那个日志条目的索引
     */
    private long prevLogIndex;

    /**
     * 紧邻新日志条目之前的那个日志条目的任期
     */
    private long prevLogTerm;

    /**
     * 需要被保存的日志条目（被当做心跳使用时，则日志条目内容为空；为了提高效率可能一次性发送多个）
     */
    private List<LogEntry> entries;

    /**
     * 领导人的已知已提交的最高的日志条目的索引
     */
    private long leaderCommit;
}
