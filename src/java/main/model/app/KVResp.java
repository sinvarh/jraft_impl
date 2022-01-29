package main.model.app;

import lombok.Data;

/**
 * @program: jraft
 * @description: 客户端key_value 请求
 * @author: hxh E-mail:hxh@100cb.cn
 * @create: 2022-01-10 13:23
 */
@Data
public class KVResp {
    private int code;
    private String msg;
}
