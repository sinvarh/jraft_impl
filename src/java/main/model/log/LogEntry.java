package main.model.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sinvar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry implements Serializable {
    private static final long serialVersionUID = 7402903477645853969L;
    private Long index;
    private Integer term;

    private Command command;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Command implements Serializable{
        private static final long serialVersionUID = -9213794301986127792L;
        private String key;
        private String value;
    }
}
