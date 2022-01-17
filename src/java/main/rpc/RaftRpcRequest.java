package main.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RaftRpcRequest implements Serializable {
    /** for serialization */
    private static final long  serialVersionUID          = -1;


    /**
     * 请求的类型
     */
    private int commandType;

    /**
     * 携带的数据
     */
    private Object object;

}
