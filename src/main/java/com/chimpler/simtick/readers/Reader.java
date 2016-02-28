package com.chimpler.simtick.readers;

import com.chimpler.simtick.writers.LongWriter;

public abstract class Reader<T> {
    // reuse same object to avoid reinstantiation every time
    protected final ValueAndLength<T> valueAndLength;
    public final boolean fixed;


    public abstract ValueAndLength<T> readRaw(byte[] buffer, int offset);

    public abstract ValueAndLength<T> readDelta(byte[] buffer, int offset);

    public Reader(boolean fixed) {
        this.fixed = fixed;
        valueAndLength = new ValueAndLength();
    }
}
