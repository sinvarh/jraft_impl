package main.model.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {
    private Long index;
    private Integer term;

    private Command command;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Command {
        private String key;
        private String value;
    }
}
