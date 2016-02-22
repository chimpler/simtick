package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;

public class VarCharWriter extends Writer<String> {
    private final BitCodec bitCodec;
    private final int lenBits; // number of bits to encode the length

    public VarCharWriter(int maxChars) {
        super(false);
        this.bitCodec = new BitCodec();
        this.lenBits = (int)Math.ceil(Math.log(maxChars) / Math.log(2));
    }

    @Override
    public boolean isDelta(String value) {
        return true;
    }

    private int writeValue(byte[] buffer, String value, int offset) {
        int strLen = value.length();

        // write length
        bitCodec.write(buffer, strLen, offset, lenBits);

        // write string
        bitCodec.writeBytes(buffer, value.getBytes(), offset + lenBits);
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
}
