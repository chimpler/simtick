package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;

public class VarCharReader extends Reader<String> {
    private final byte[] tmpBuffer;
    private final int lenBits; // number of bits to encode the length

    public VarCharReader(short maxChars) {
        super(false);
        this.lenBits = (int) Math.ceil(Math.log(maxChars) / Math.log(2));
        this.tmpBuffer = new byte[maxChars];
    }

    private ValueAndLength<String> readValue(byte[] buffer, int offset) {
        // write length
        int strLen = (int) BitCodec.read(buffer, offset, lenBits);

        // write string
        return this.valueAndLength.withValueAndLength(
                new String(BitCodec.readBytes(buffer, offset + lenBits, tmpBuffer, strLen), 0, strLen),
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
