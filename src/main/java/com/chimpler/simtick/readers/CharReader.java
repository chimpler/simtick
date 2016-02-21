package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;

public class CharReader extends Reader<String> {
    private final BitCodec codec;
    private final int numChars;
    private final byte[] tmpBuffer;

    public CharReader(int numChars) {
        this.codec = new BitCodec();
        this.numChars = numChars;
        this.tmpBuffer = new byte[numChars];
    }

    private ValueAndLength<String> readValue(byte[] buffer, int offset) {
        return this.valueAndLength.withValueAndLength(
                new String(codec.readBytes(buffer, offset, tmpBuffer, numChars), 0, numChars),
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
}
