package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.LongCodec;

public class LongWriter extends Writer<Long> {
    private LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private int rawBits;
    private int deltaBits;

    public LongWriter(int rawBits, int deltaBits, boolean unsignedRaw, boolean unsignedDelta) {
        this.codec = new LongCodec(rawBits, deltaBits, unsignedRaw, unsignedDelta);
        this.rawBits = rawBits;
        this.deltaBits = deltaBits;
    }

    @Override
    public boolean isDelta(Long value) {
        return this.codec.isInDeltaRange(oldValue, value);
    }

    @Override
    public int writeRaw(byte[] buffer, Long value, int offset) {
        codec.writeRawValue(buffer, value, offset);
        oldValue = value;
        return rawBits;
    }

    @Override
    public int writeDelta(byte[] buffer, Long value, int offset) {
        codec.writeDeltaValue(buffer, value - oldValue, offset);
        oldValue = value;
        return deltaBits;
    }
}
