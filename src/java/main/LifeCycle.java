package main;

public interface LifeCycle<T> {
    /**
     * 初始化
     * @param opts
     * @return
     */
    boolean init(final T opts);

    /**
     * shutdown
     * @return
     */
    boolean shutdown();
}
