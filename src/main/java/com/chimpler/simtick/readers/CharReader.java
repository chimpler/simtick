package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;

public class CharReader extends Reader<String> {
    private final BitCodec codec;
    private final int numChars;
    private ThreadLocal<byte[]> tmpBuffer = new ThreadLocal<byte[]>() {
        @Override
        protected byte[] initialValue() {
            return new byte[numChars];
        }
    };

    public CharReader(int numChars) {
        this.codec = new BitCodec();
        this.numChars = numChars;
    }

    private String readValue(byte[] buffer, int offset) {
        return new String(codec.readBytes(buffer, offset, tmpBuffer.get(), numChars), 0, numChars);
    }

    @Override
    public String readRaw(byte[] buffer, int offset) {
        return readValue(buffer, offset);
    }

    @Override
    public String readDelta(byte[] buffer, int offset) {
        return readValue(buffer, offset);
    }
}
