package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;

public class LongReader extends Reader<Long> {
    private LongCodec codec;

    public LongReader(long minRaw, long maxRaw) {
        this(minRaw, maxRaw, true, 0, 0);
    }

    public LongReader(long minRaw, long maxRaw, long minDelta, long maxDelta) {
        this(minRaw, maxRaw, false, minDelta, maxDelta);
    }

    private LongReader(long minRaw, long maxRaw, boolean fixed, long minDelta, long maxDelta) {
        super(fixed, 321);
        codec = new CodecFactory().buildDeltaLongCodec(minRaw, maxRaw, minDelta, maxDelta, false);
    }

    public static ValueAndLength<Reader> fromByteArray(byte[] buffer, int offset) {
        return new ValueAndLength<Reader>(
                new LongReader(
                        BitCodec.read(buffer, offset, 64),
                        BitCodec.read(buffer, offset + 64, 64),
                        BitCodec.read(buffer, offset + 128, 1) == 1,
                        BitCodec.read(buffer, offset + 129, 32),
                        BitCodec.read(buffer, offset + 161, 32)
                ),
                193
        );
    }

    @Override
    public ValueAndLength<Long> readRaw(byte[] buffer, int offset) {
        return this.valueAndLength.withValueAndLength(
                codec.readRawValue(buffer, offset),
                codec.rawBits
        );
    }

    @Override
    public ValueAndLength<Long> readDelta(byte[] buffer, int offset) {
        Long delta = codec.readDeltaValue(buffer, offset);
        return this.valueAndLength.withValueAndLength(
                this.valueAndLength.value + delta,
                codec.deltaBits
        );
    }

    @Override
    public String toString() {
        return "LongReader{" +
                "codec=" + codec +
                ", fixed=" + fixed +
                ", headerSize=" + headerSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongReader that = (LongReader) o;

        return codec != null ? codec.equals(that.codec) : that.codec == null;
    }

    @Override
    public int hashCode() {
        return codec != null ? codec.hashCode() : 0;
    }
}
