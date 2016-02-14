package com.chimpler.simtick.writers;

public abstract class Writer<T> {
    public abstract boolean isDelta(T value);
    public abstract int writeRaw(byte[] buffer, T t, int offset);
    public abstract int writeDelta(byte[] buffer, T t, int offset);
}
