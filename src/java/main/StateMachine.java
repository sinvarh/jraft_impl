package main;


import main.entity.LogEntry;

/**
 * 复制状态机interface
 */
public interface StateMachine {

    void apply(LogEntry logEntry);
}
