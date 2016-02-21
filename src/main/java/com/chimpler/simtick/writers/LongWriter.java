package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class LongWriter extends Writer<Long> {
    private LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;

    public LongWriter(long minRaw, long maxRaw, long minDelta, long maxDelta) {
        codec = new CodecFactory().buildLongCodec(minRaw, maxRaw, minDelta, maxDelta);
    }

    @Override
    public boolean isDelta(Long value) {
        return this.codec.isInDeltaRange(oldValue, value);
    }

    @Override
    public int writeRaw(byte[] buffer, Long value, int offset) {
        codec.writeRawValue(buffer, value, offset);
        oldValue = value;
        return codec.rawBits;
    }

    @Override
    public int writeDelta(byte[] buffer, Long value, int offset) {
        codec.writeDeltaValue(buffer, value - oldValue, offset);
        oldValue = value;
        return codec.deltaBits;
    }
}
