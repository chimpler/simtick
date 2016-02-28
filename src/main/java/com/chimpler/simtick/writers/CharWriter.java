package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;

public class CharWriter extends Writer<String> {
    public static final byte TYPE_ID = 3;

    private final short numChars;

    public CharWriter(short numChars) {
        super(true);
        this.numChars = numChars;
    }

    @Override
    public boolean isDelta(String value) {
        return true;
    }

    private int writeValue(byte[] buffer, String value, int offset) {
        BitCodec.writeBytes(buffer, value.getBytes(), offset);
        return numChars * 8;
    }

    @Override
    public int writeRaw(byte[] buffer, String value, int offset) {
        return this.writeValue(buffer, value, offset);
    }

    @Override
    public int writeDelta(byte[] buffer, String value, int offset) {
        return this.writeValue(buffer, value, offset);
    }

    @Override
    public int writerHeader(byte[] buffer, int srcOffset) {
        int offset = srcOffset;
        offset += BitCodec.write(buffer, TYPE_ID, offset, 7);
        offset += BitCodec.write(buffer, numChars, offset, 15);
        return offset - srcOffset;
    }

}
