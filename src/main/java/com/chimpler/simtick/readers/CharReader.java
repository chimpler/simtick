package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;

public class CharReader extends Reader<String> {
    private final int numChars;

    public CharReader(byte[] buffer, int offset) {
        this((int)BitCodec.read(buffer, offset, 64));
    }

    public CharReader(int numChars) {
        super(true, 2);
        this.numChars = numChars;
    }

    private ValueAndLength<String> readValue(byte[] buffer, int offset) {
        return this.valueAndLength.withValueAndLength(
                new String(BitCodec.readBytes(buffer, offset, buffer, numChars), 0, numChars),
                numChars * 8
        );
    }

    @Override
    public ValueAndLength<String> readRaw(byte[] buffer, int offset) {
        return readValue(buffer, offset);
    }

    @Override
    public ValueAndLength<String> readDelta(byte[] buffer, int offset) {
        return readValue(buffer, offset);
    }

    @Override
    public String toString() {
        return String.format("[CharReader numChars=%d]", numChars);
    }
}
