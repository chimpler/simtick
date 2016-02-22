package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;

public class CharWriter extends Writer<String> {
    private final BitCodec bitCodec;
    private final int numChars;

    public CharWriter(int numChars) {
        super(true);
        this.bitCodec = new BitCodec();
        this.numChars = numChars;
    }

    @Override
    public boolean isDelta(String value) {
        return true;
    }

    private int writeValue(byte[] buffer, String value, int offset) {
        bitCodec.writeBytes(buffer, value.getBytes(), offset);
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
}
