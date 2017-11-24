package com.chimpler.simtick.codec;

public class LongCodec {
    public int rawBits;
    public int deltaBits;
    private long minRaw;
    private long maxRaw;
    private long minDelta;
    private long maxDelta;

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
        BitCodec.write(buffer, value - minRaw, offset, rawBits);
        return rawBits;
    }

    public long readRawValue(byte[] buffer, int offset) {
        long value = BitCodec.read(buffer, offset, rawBits);
        return value + minRaw;
    }

    public int writeDeltaValue(byte[] buffer, long value, int offset) {
        BitCodec.write(buffer, value - minDelta, offset, deltaBits);
        return deltaBits;
    }

    public long readDeltaValue(byte[] buffer, int offset) {
        long value = BitCodec.read(buffer, offset, deltaBits);
        return value + minDelta;
    }

    public int getRawBits() {
        return rawBits;
    }

    public void setRawBits(int rawBits) {
        this.rawBits = rawBits;
    }

    public int getDeltaBits() {
        return deltaBits;
    }

    public void setDeltaBits(int deltaBits) {
        this.deltaBits = deltaBits;
    }

    public long getMinRaw() {
        return minRaw;
    }

    public void setMinRaw(long minRaw) {
        this.minRaw = minRaw;
    }

    public long getMaxRaw() {
        return maxRaw;
    }

    public void setMaxRaw(long maxRaw) {
        this.maxRaw = maxRaw;
    }

    public long getMinDelta() {
        return minDelta;
    }

    public void setMinDelta(long minDelta) {
        this.minDelta = minDelta;
    }

    public long getMaxDelta() {
        return maxDelta;
    }

    public void setMaxDelta(long maxDelta) {
        this.maxDelta = maxDelta;
    }

    @Override
    public String toString() {
        return "LongCodec{" +
                "rawBits=" + rawBits +
                ", deltaBits=" + deltaBits +
                ", minRaw=" + minRaw +
                ", maxRaw=" + maxRaw +
                ", minDelta=" + minDelta +
                ", maxDelta=" + maxDelta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongCodec longCodec = (LongCodec) o;

        if (rawBits != longCodec.rawBits) return false;
        if (deltaBits != longCodec.deltaBits) return false;
        if (minRaw != longCodec.minRaw) return false;
        if (maxRaw != longCodec.maxRaw) return false;
        if (minDelta != longCodec.minDelta) return false;
        return maxDelta == longCodec.maxDelta;
    }

    @Override
    public int hashCode() {
        int result = rawBits;
        result = 31 * result + deltaBits;
        result = 31 * result + (int) (minRaw ^ (minRaw >>> 32));
        result = 31 * result + (int) (maxRaw ^ (maxRaw >>> 32));
        result = 31 * result + (int) (minDelta ^ (minDelta >>> 32));
        result = 31 * result + (int) (maxDelta ^ (maxDelta >>> 32));
        return result;
    }
}
