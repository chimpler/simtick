package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;

public class VarCharWriter extends Writer<String> {
    public static final byte TYPE_ID = 4;

    private final int lenBits; // number of bits to encode the length
    private final short maxChars;

    public VarCharWriter(short maxChars) {
        super(false);
        this.maxChars = maxChars;
        this.lenBits = (int)Math.ceil(Math.log(maxChars) / Math.log(2));
    }

    @Override
    public boolean isDelta(String value) {
        return true;
    }

    private int writeValue(byte[] buffer, String value, int offset) {
        int strLen = value.length();

        // write length
        BitCodec.write(buffer, strLen, offset, lenBits);

        // write string
        BitCodec.writeBytes(buffer, value.getBytes(), offset + lenBits);
        return strLen * 8 + lenBits;
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
    public int writerHeader(byte[] buffer, int offset) {
        BitCodec.write(buffer, maxChars, offset, 16);
        return 0;
    }

    @Override
    public int getMaxSize() {
        return lenBits + maxChars * 8;
    }
}
