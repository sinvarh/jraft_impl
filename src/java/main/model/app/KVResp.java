package main.model.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: jraft
 * @description: 客户端key_value 请求
 * @author: hxh E-mail:hxh@100cb.cn
 * @create: 2022-01-10 13:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KVResp implements Serializable {
    private int code;
    private String msg;
}
