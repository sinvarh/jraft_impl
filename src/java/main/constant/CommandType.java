package main.constant;

import lombok.Getter;

/**
 * @program: jraft
 * @description: 请求类型
 * @author: hxh E-mail:hxh@100cb.cn
 * @create: 2022-01-31 11:54
 */
@Getter
public enum CommandType {
    vote(1, "投票"),
    appendLog(2, "增加日志"),

    /**
     * 客户端请求
     * @param type
     * @param desc
     */
    addKv(3,"写入kv"),
    readKv(4,"读取数值");

    CommandType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    int type;
    String desc;
}
