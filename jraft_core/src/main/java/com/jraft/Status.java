package com.jraft;

import com.jraft.error.RaftError;
import com.jraft.util.Copiable;

import java.util.Objects;

public class Status implements Copiable<Status> {

    private State state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(state, status.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    /**
     * 状态内部类
     */

    private static class State {
        /**
         * error code
         */
        int code;
        /**
         * error msg
         */
        String msg;

        public State(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return code == state.code &&
                    Objects.equals(msg, state.msg);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, msg);
        }
    }


    public Status() {
        this.state = null;
    }

    public static Status OK() {
        return new Status();
    }

    /**
     * 传status进来
     *
     * @param s
     */
    public Status(Status s) {
        if (s.state != null) {
            this.state = new State(s.state.getCode(), s.state.getMsg());

        } else {
            this.state = null;
        }
    }

    public Status(RaftError raftError, String fmt, Object... args) {
        this.state = new State(raftError.getNumber(), String.format(fmt, args));
    }

    public Status(int code, String fmt, Object... args) {
        this.state = new State(code, String.format(fmt, args));
    }


    public Status(int code, String msg) {
        this.state = new State(code, msg);
    }

    @Override
    public Status copy() {
        return new Status(this.state.code, this.state.msg);
    }
}

