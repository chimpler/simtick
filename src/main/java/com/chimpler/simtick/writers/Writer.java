package com.chimpler.simtick.writers;

public abstract class Writer<T> {
    protected int rawBits;
    protected int deltaBits;

    public Writer() {}

    public Writer(int rawBits, int deltaBits) {
        this.rawBits = rawBits;
        this.deltaBits = deltaBits;
    }

    public abstract boolean isDelta(T value);
    public abstract int writeRaw(byte[] buffer, T t, int offset);
    public abstract int writeDelta(byte[] buffer, T t, int offset);
}
