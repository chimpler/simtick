package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class DecimalReader extends Reader<Double> {
    private LongCodec codec;
    private Long oldValue;
    private double divFactor;

    public DecimalReader(double minRaw, double maxRaw, double minDelta, double maxDelta, int decimalMark) {
        this.divFactor = (int) Math.pow(10, decimalMark);
        this.codec = new CodecFactory().buildLongCodec(
                (long) (minRaw * divFactor),
                (long) (maxRaw * divFactor),
                (long) (minDelta * divFactor),
                (long) (maxDelta * divFactor)
        );
    }

    @Override
    public ValueAndLength<Double> readRaw(byte[] buffer, int offset) {
        this.oldValue = codec.readRawValue(buffer, offset);
        return this.valueAndLength.withValueAndLength(
                oldValue / divFactor,
                codec.rawBits
        );
    }

    @Override
    public ValueAndLength<Double> readDelta(byte[] buffer, int offset) {
        Long delta = codec.readDeltaValue(buffer, offset);
        this.oldValue += delta;
        return this.valueAndLength.withValueAndLength(
                oldValue / divFactor,
                codec.deltaBits
        );
    }
}