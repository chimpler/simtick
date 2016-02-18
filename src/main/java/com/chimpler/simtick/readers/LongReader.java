package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.LongCodec;

public class LongReader extends Reader<Long> {
    private LongCodec codec;
    private Long oldValue;

    public LongReader(int rawBits, int deltaBits, boolean unsignedRaw, boolean unsignedDelta) {
        this.rawBits = rawBits;
        this.deltaBits = deltaBits;
        this.codec = new LongCodec(rawBits, deltaBits, unsignedRaw, unsignedDelta);
    }

    @Override
    public Long readRaw(byte[] buffer, int offset) {
        this.oldValue = codec.readRawValue(buffer, offset);
        return oldValue;
    }

    @Override
    public Long readDelta(byte[] buffer, int offset) {
        Long delta = codec.readDeltaValue(buffer, offset);
        this.oldValue += delta;
        return this.oldValue;
    }
}
