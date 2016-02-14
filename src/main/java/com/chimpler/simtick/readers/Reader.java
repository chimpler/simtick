package com.chimpler.simtick.readers;

public abstract class Reader<T> {
    public abstract T readRaw(byte[] buffer, int offset);
    public abstract T readDelta(byte[] buffer, int offset);
}
