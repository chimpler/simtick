package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class LongReader extends Reader<Long> {
    private LongCodec codec;

    public LongReader(long minRaw, long maxRaw) {
        this(minRaw, maxRaw, true, 0, 0);
    }

    public LongReader(long minRaw, long maxRaw, long minDelta, long maxDelta) {
        this(minRaw, maxRaw, false, minDelta, maxDelta);
    }

    private LongReader(long minRaw, long maxRaw, boolean fixed, long minDelta, long maxDelta) {
        super(fixed);
        codec = new CodecFactory().buildDeltaLongCodec(minRaw, maxRaw, minDelta, maxDelta, false);
    }

    @Override
    public ValueAndLength<Long> readRaw(byte[] buffer, int offset) {
        return this.valueAndLength.withValueAndLength(
                codec.readRawValue(buffer, offset),
                codec.rawBits
        );
    }

    @Override
    public ValueAndLength<Long> readDelta(byte[] buffer, int offset) {
        Long delta = codec.readDeltaValue(buffer, offset);
        return this.valueAndLength.withValueAndLength(
                this.valueAndLength.value + delta,
                codec.deltaBits
        );
    }
}
