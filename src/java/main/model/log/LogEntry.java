package main.model.log;

import lombok.Data;

@Data
public class LogEntry {
    private Long index;
    private Integer term;

    private Command command;

    @Data
    public class Command {
        private String key;
        private String value;
    }
}
