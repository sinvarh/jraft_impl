package main.model.rpc.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RaftRpcResp implements Serializable {
    private static final long  serialVersionUID          = -1;

    private int code;
    private Object data;
}
