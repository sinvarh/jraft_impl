package com.jraft;

import com.jraft.error.RaftException;

public interface FSMCaller extends LifeCycle<FSMCallerOptions>,Describer{

    boolean onCommitted(final long committedIndex);

    boolean onSnapshotLoad(final LoadSnapshotClosure done);

    boolean onSnapshotSave(final SaveSnapshotClosure done);

    boolean onLeaderStop(final Status status);

    boolean onLeaderStart(final long term);

    boolean onStartFollowing(final LeaderChangeContext ctx);

    boolean onStopFollowing(final LeaderChangeContext ctx);

    boolean onError(final RaftException error);

    /**
     * 获取最后获取的index
     * @return
     */
    long getLastAppliedIndex();

    /**
     * Listen on lastAppliedLogIndex update events.
     *
     * @author dennis
     */
    interface LastAppliedLogIndexListener{

        /**
         * Called when lastAppliedLogIndex updated.
         *
         * @param lastAppliedLogIndex the log index of last applied
         */
        void onApplied(final long lastAppliedLogIndex);
    }

    /**
     * 增加listener
     * @param listener
     */
    void addLastAppliedLogIndexListener(final LastAppliedLogIndexListener listener);
}
