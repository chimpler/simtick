package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class LongReader extends Reader<Long> {
    private LongCodec codec;

    public LongReader(long minRaw, long maxRaw, long minDelta, long maxDelta) {
        codec = new CodecFactory().buildLongCodec(minRaw, maxRaw, minDelta, maxDelta);
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
