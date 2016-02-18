package com.chimpler.simtick.codec;

public class LongCodec {
    private long maxRaw;
    private long minRaw;
    private long maxDelta;
    private long minDelta;
    private int rawBits;
    private int deltaBits;
    private long halfMaxRaw;
    private long halfMaxDelta;
    private BitCodec bitCodec = new BitCodec();

    public LongCodec(int rawBits, int deltaBits, boolean unsignedRaw, boolean unsignedDelta) {
        this.rawBits = rawBits;
        this.deltaBits = deltaBits;

        int unsignedMaxRaw = (1 << rawBits) - 1;
        int unsignedMaxDelta = (1 << deltaBits) - 1;

        if (unsignedRaw) {
            this.maxRaw = unsignedMaxRaw;
            this.minRaw = 0;
        } else {
            this.maxRaw = unsignedMaxRaw >> 1;
            this.minRaw = -unsignedMaxRaw >> 1;
            this.halfMaxRaw = unsignedMaxRaw / 2;
        }

        if (unsignedDelta) {
            this.maxDelta = unsignedMaxDelta;
            this.minDelta = 0;
        } else {
            this.maxDelta = unsignedMaxDelta >> 1;
            this.minDelta = -(unsignedMaxDelta >> 1) - 1;
            this.halfMaxDelta = unsignedMaxDelta / 2;
        }
    }

    public boolean isInDeltaRange(long oldValue, long newValue) {
        long diff = newValue - oldValue;
        return minDelta <= diff && diff <= maxDelta;
    }

    public boolean isInRawRange(long oldValue, long newValue) {
        long diff = newValue - oldValue;
        return minRaw <= diff && diff <= maxRaw;
    }

    public int writeRawValue(byte[] buffer, long value, int offset) {
        bitCodec.write(buffer, value + halfMaxRaw, offset, rawBits);
        return rawBits;
    }

    public long readRawValue(byte[] buffer, int offset) {
        long value = bitCodec.read(buffer, offset, rawBits);
        return value - halfMaxRaw;
    }

    public int writeDeltaValue(byte[] buffer, long value, int offset) {
        bitCodec.write(buffer, value + halfMaxDelta, offset, deltaBits);
        return deltaBits;
    }

    public long readDeltaValue(byte[] buffer, int offset) {
        long value = bitCodec.read(buffer, offset, deltaBits);
        return value - halfMaxDelta;
    }
}
