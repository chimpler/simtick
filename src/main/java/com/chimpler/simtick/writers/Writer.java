package com.chimpler.simtick.writers;

public abstract class Writer<T> {
    protected final boolean fixed;

    public Writer(boolean fixed) {
        this.fixed = fixed;
    }

    public abstract boolean isDelta(T value);
    public abstract int writeRaw(byte[] buffer, T t, int offset);
    public abstract int writeDelta(byte[] buffer, T t, int offset);
    public abstract int writerHeader(byte[] buffer, int offset);
}
