package com.chimpler.simtick.readers;

public abstract class Reader<T> {
    protected int rawBits;
    protected int deltaBits;
    protected final ValueAndLength<T> valueAndLength;

    public abstract ValueAndLength<T> readRaw(byte[] buffer, int offset);
    public abstract ValueAndLength<T> readDelta(byte[] buffer, int offset);

    public Reader() {
        valueAndLength = new ValueAndLength();
    }

    public Reader(int rawBits, int deltaBits) {
        valueAndLength = new ValueAndLength();
        this.rawBits = rawBits;
        this.deltaBits = deltaBits;
    }
}
