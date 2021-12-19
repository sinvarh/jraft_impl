package main.entity;

import lombok.Data;

@Data
public class LogEntry {
    private long index;
    private int term;

    private Command command;
}
