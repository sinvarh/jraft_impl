package main.rpc;

import lombok.Data;

import java.io.Serializable;

@Data
public class RaftResponse<T> implements Serializable {
    private static final long  serialVersionUID          = -1;

    private T data;
}
