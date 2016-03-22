package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class LongWriter extends Writer<Long> {
    public static final byte TYPE_ID = 1;

    private LongCodec longCodec;
    private Long oldValue = Long.MAX_VALUE;
    private long minRaw;
    private long maxRaw;
    private boolean fixed;
    private long minDelta;
    private long maxDelta;

    public LongWriter(long minRaw, long maxRaw) {
        this(minRaw, maxRaw, true, 0, 0);
    }

    public LongWriter(long minRaw, long maxRaw, long minDelta, long maxDelta) {
        this(minRaw, maxRaw, false, minDelta, maxDelta);
    }

    private LongWriter(long minRaw, long maxRaw, boolean fixed, long minDelta, long maxDelta) {
        super(fixed);
        this.minRaw = minRaw;
        this.maxRaw = maxRaw;
        this.fixed = fixed;
        this.minDelta = minDelta;
        this.maxDelta = maxDelta;
        longCodec = new CodecFactory().buildDeltaLongCodec(minRaw, maxRaw, minDelta, maxDelta, false);
    }

    @Override
    public boolean isDelta(Long value) {
        return this.longCodec.isInDeltaRange(oldValue, value);
    }

    @Override
    public int writeRaw(byte[] buffer, Long value, int offset) {
        longCodec.writeRawValue(buffer, value, offset);
        oldValue = value;
        return longCodec.rawBits;
    }

    @Override
    public int writeDelta(byte[] buffer, Long value, int offset) {
        longCodec.writeDeltaValue(buffer, value - oldValue, offset);
        oldValue = value;
        return longCodec.deltaBits;
    }

    @Override
    public int writerHeader(byte[] buffer, int srcOffset) {
        int offset = srcOffset;
        offset += BitCodec.write(buffer, minRaw, offset, 64);
        offset += BitCodec.write(buffer, maxRaw, offset, 64);
        offset += BitCodec.write(buffer, fixed ? 1:0, offset, 1);
        offset += BitCodec.write(buffer, minDelta, offset, 64);
        offset += BitCodec.write(buffer, maxDelta, offset, 64);
        return offset - srcOffset;
    }

    @Override
    public int getMaxSize() {
        return longCodec.rawBits;
    }
}
