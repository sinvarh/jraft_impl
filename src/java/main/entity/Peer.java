package main.entity;

import java.util.Objects;

public class Peer {
    @Override
    public String toString() {
        return "Peer{" +
                "addr='" + addr + '\'' +
                '}';
    }

    private final String addr;

    public Peer(String addr) {
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peer peer = (Peer) o;
        return addr.equals(peer.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addr);
    }
}
