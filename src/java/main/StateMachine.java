package main;


import main.model.log.LogEntry;

/**
 * 复制状态机interface
 */
public interface StateMachine {

    void apply(LogEntry logEntry);
}
