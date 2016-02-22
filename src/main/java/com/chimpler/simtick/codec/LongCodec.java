package com.chimpler.simtick.codec;

public class LongCodec {
    public int rawBits;
    public int deltaBits;
    private long minRaw;
    private long maxRaw;
    private long minDelta;
    private long maxDelta;
    private BitCodec bitCodec = new BitCodec();

    public LongCodec(int rawBits, int deltaBits, long minRaw, long minDelta) {
        this.rawBits = rawBits;
        this.deltaBits = deltaBits;

        this.minRaw = minRaw;
        this.minDelta = minDelta;
        this.maxRaw = minRaw + 1 << rawBits;
        this.maxDelta = minDelta + 1 << rawBits;
    }

    public LongCodec(int rawBits, long minRaw) {
        this(rawBits, 0, minRaw, 0L);
    }

    public boolean isInDeltaRange(long oldValue, long newValue) {
        long diff = newValue - oldValue;
        return minDelta <= diff && diff <= maxDelta;
    }

    public boolean isInRawRange(long newValue) {
        return minRaw <= newValue && newValue <= maxRaw;
    }

    public int writeRawValue(byte[] buffer, long value, int offset) {
        bitCodec.write(buffer, value - minRaw, offset, rawBits);
        return rawBits;
    }

    public long readRawValue(byte[] buffer, int offset) {
        long value = bitCodec.read(buffer, offset, rawBits);
        return value + minRaw;
    }

    public int writeDeltaValue(byte[] buffer, long value, int offset) {
        bitCodec.write(buffer, value - minDelta, offset, deltaBits);
        return deltaBits;
    }

    public long readDeltaValue(byte[] buffer, int offset) {
        long value = bitCodec.read(buffer, offset, deltaBits);
        return value + minDelta;
    }
}
