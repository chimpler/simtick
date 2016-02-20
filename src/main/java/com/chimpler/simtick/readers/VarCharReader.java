package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;

public class VarCharReader extends Reader<String> {
    private final BitCodec bitCodec;
    private final byte[] tmpBuffer;
    private final int lenBits; // number of bits to encode the length

    public VarCharReader(int maxChars) {
        this.bitCodec = new BitCodec();
        this.lenBits = (int) Math.ceil(Math.log(maxChars) / Math.log(2));
        this.tmpBuffer = new byte[maxChars];
    }

    private ValueAndLength<String> readValue(byte[] buffer, int offset) {
        // write length
        int strLen = (int) bitCodec.read(buffer, offset, lenBits);

        // write string
        return this.valueAndLength.withValueAndLength(
                new String(bitCodec.readBytes(buffer, offset + lenBits, tmpBuffer, strLen), 0, strLen),
                lenBits + strLen * 8

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
