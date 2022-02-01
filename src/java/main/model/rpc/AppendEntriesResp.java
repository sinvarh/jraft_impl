package main.model.rpc;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 追加日志的结果
 *
 * @author sinvar
 */
@Data
@Builder
public class AppendEntriesResp implements Serializable {
    private static final long serialVersionUID = 1736697706911983935L;
    /**
     * 当前任期，对于领导人而言 它会更新自己的任期
     */
    private int term;

    /**
     * 如果跟随者所含有的条目和 prevLogIndex 以及 prevLogTerm 匹配上了，则为 true
     */
    private Boolean success;
}
