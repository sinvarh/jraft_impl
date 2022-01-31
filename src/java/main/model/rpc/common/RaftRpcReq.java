package main.model.rpc.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RaftRpcReq implements Serializable {
    /** for serialization */
    private static final long  serialVersionUID          = -1;

    /**
     * 操作類型
     */
    private int type;
    /**
     * 携带的数据
     */
    private Object data;

}
