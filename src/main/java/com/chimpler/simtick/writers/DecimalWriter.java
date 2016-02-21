package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class DecimalWriter extends Writer<Double> {
    private LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private double divFactor;

    public DecimalWriter(double minRaw, double maxRaw, double minDelta, double maxDelta, int decimalMark) {
        this.divFactor = (int) Math.pow(10, decimalMark);
        this.codec = new CodecFactory().buildLongCodec(
                (long) (minRaw * divFactor),
                (long) (maxRaw * divFactor),
                (long) (minDelta * divFactor),
                (long) (maxDelta * divFactor)
        );
    }

    @Override
    public boolean isDelta(Double value) {
        long longValue = (long)(value * divFactor);
        return this.codec.isInDeltaRange(oldValue, longValue);
    }

    @Override
    public int writeRaw(byte[] buffer, Double value, int offset) {
        this.oldValue = (long)(value * divFactor);
        codec.writeRawValue(buffer, oldValue, offset);
        return codec.rawBits;
    }

    @Override
    public int writeDelta(byte[] buffer, Double value, int offset) {
        long longValue = (long)(value * divFactor);
        long delta = longValue - oldValue;
        this.oldValue = longValue;
        codec.writeDeltaValue(buffer, delta, offset);
        return codec.deltaBits;
    }
}
