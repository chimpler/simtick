package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.LongCodec;

public class DecimalReader extends Reader<Double> {
    private LongCodec codec;
    private Long oldValue;
    private double divFactor;

    public DecimalReader(int rawBits, int deltaBits, boolean unsignedRaw, boolean unsignedDelta, int decimalMark) {
        this.divFactor = (int)Math.pow(10, decimalMark);
        this.codec = new LongCodec(rawBits, deltaBits, unsignedRaw, unsignedDelta);
    }

    @Override
    public Double readRaw(byte[] buffer, int offset) {
        this.oldValue = codec.readRawValue(buffer, offset);
        return oldValue / divFactor;
    }

    @Override
    public Double readDelta(byte[] buffer, int offset) {
        Long delta = codec.readDeltaValue(buffer, offset);
        this.oldValue += delta;
        return this.oldValue / divFactor;
    }
}
