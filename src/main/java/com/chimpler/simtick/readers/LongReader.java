package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.LongCodec;

public class LongReader extends Reader<Long> {
    private LongCodec codec;

    public LongReader(int rawBits, int deltaBits, boolean unsignedRaw, boolean unsignedDelta) {
        super(rawBits, deltaBits);
        this.codec = new LongCodec(rawBits, deltaBits, unsignedRaw, unsignedDelta);
    }

    @Override
    public ValueAndLength<Long> readRaw(byte[] buffer, int offset) {
        return this.valueAndLength.withValueAndLength(
                codec.readRawValue(buffer, offset),
                rawBits
        );
    }

    @Override
    public ValueAndLength<Long> readDelta(byte[] buffer, int offset) {
        Long delta = codec.readDeltaValue(buffer, offset);
        return this.valueAndLength.withValueAndLength(
                this.valueAndLength.value + delta,
                deltaBits
        );
    }
}
