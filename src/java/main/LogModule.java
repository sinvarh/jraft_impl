package main;

import main.entity.LogEntry;

import java.util.List;

public interface LogModule {
    /**
     * 追加本地日志
     * @param entries
     */
    void appendEntries(final List<LogEntry> entries);

    /**
     * 获取最新的log
     * @return
     */
    LogEntry getLast();


    long getLastIndex();

    LogEntry read(Long index);

    boolean removeFromStartIndex(Long startIndex);

    void write(LogEntry logEntry);


}
