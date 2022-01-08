package main;

import main.entity.LogEntry;

import java.util.List;

public class DefaultLogModule implements LogModule{
    @Override
    public void appendEntries(List<LogEntry> entries) {

    }

    @Override
    public LogEntry getLast() {
        return null;
    }

    @Override
    public long getLastIndex() {
        return 0;
    }

    @Override
    public LogEntry read(Long index) {
        return null;
    }

    @Override
    public boolean removeFromStartIndex(Long startIndex) {
        return false;
    }

    @Override
    public void write(LogEntry logEntry) {

    }
}
