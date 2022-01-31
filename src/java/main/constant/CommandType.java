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
    appendLog(2, "增加日志");

    CommandType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    int type;
    String desc;
}
