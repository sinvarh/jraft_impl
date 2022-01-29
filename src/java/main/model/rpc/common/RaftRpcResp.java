package main.model.rpc.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RaftRpcResp<T> implements Serializable {
    private static final long  serialVersionUID          = -1;

    private boolean res;
    private T data;
}
