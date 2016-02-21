package com.chimpler.simtick.readers;

public abstract class Reader<T> {
    // reuse same object to avoid reinstantiation every time
    protected final ValueAndLength<T> valueAndLength;

    public abstract ValueAndLength<T> readRaw(byte[] buffer, int offset);
    public abstract ValueAndLength<T> readDelta(byte[] buffer, int offset);

    public Reader() {
        valueAndLength = new ValueAndLength();
    }
}
