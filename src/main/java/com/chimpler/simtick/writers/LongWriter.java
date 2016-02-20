package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.LongCodec;

public class LongWriter extends Writer<Long> {
    private LongCodec longCodec;
    private Long oldValue = Long.MAX_VALUE;

    public LongWriter(int rawBits, int deltaBits, boolean unsignedRaw, boolean unsignedDelta) {
        super(rawBits, deltaBits);
        this.longCodec = new LongCodec(rawBits, deltaBits, unsignedRaw, unsignedDelta);
    }

    @Override
    public boolean isDelta(Long value) {
        return this.longCodec.isInDeltaRange(oldValue, value);
    }

    @Override
    public int writeRaw(byte[] buffer, Long value, int offset) {
        longCodec.writeRawValue(buffer, value, offset);
        oldValue = value;
        return rawBits;
    }

    @Override
    public int writeDelta(byte[] buffer, Long value, int offset) {
        longCodec.writeDeltaValue(buffer, value - oldValue, offset);
        oldValue = value;
        return deltaBits;
    }
}
