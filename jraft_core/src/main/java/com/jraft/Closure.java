package com.jraft;


public interface Closure {
    /**
     * 任务完成时的回调
     *
     * @param status
     */
    void run(final Status status);
}
