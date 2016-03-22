package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class DecimalWriter extends Writer<Double> {
    public static final byte TYPE_ID = 2;

    private LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private double divFactor;

    private final double minRaw;
    private final double maxRaw;
    private final boolean fixed;
    private final double minDelta;
    private final double maxDelta;
    private final byte decimalMark;

    public DecimalWriter(double minRaw, double maxRaw, byte decimalMark) {
        this(minRaw, maxRaw, true, 0, 0, decimalMark);
    }

    public DecimalWriter(double minRaw, double maxRaw, double minDelta, double maxDelta, byte decimalMark) {
        this(minRaw, maxRaw, false, minDelta, maxDelta, decimalMark);
    }

    private DecimalWriter(double minRaw, double maxRaw, boolean fixed, double minDelta, double maxDelta, byte decimalMark) {
        super(fixed);
        this.minRaw = minRaw;
        this.maxRaw = maxRaw;
        this.fixed = fixed;
        this.minDelta = minDelta;
        this.maxDelta = maxDelta;
        this.decimalMark = decimalMark;
        this.divFactor = (int) Math.pow(10, decimalMark);
        this.codec = new CodecFactory().buildDeltaLongCodec(
                (long) (minRaw * divFactor),
                (long) (maxRaw * divFactor),
                (long) (minDelta * divFactor),
                (long) (maxDelta * divFactor),
                false
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

    @Override
    public int writerHeader(byte[] buffer, int srcOffset) {
        int offset = srcOffset;
        offset += BitCodec.write(buffer, (long)(minRaw * divFactor), offset, 64);
        offset += BitCodec.write(buffer, (long)(maxRaw * divFactor), offset, 64);
        offset += BitCodec.write(buffer, fixed ? 1:0, offset, 1);
        offset += BitCodec.write(buffer, (long)(minDelta * divFactor), offset, 64);
        offset += BitCodec.write(buffer, (long)(maxDelta * divFactor), offset, 64);
        offset += BitCodec.write(buffer, decimalMark, offset, 7);
        return offset - srcOffset;
    }

    @Override
    public int getMaxSize() {
        return codec.rawBits;
    }
}
