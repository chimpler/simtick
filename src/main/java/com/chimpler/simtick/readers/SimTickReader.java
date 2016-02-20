package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;

public class SimTickReader {
    private Reader[] readers;
    private BitCodec bitCodec = new BitCodec();

    public SimTickReader(Reader[] readers) {
        this.readers = readers;
    }

    private int readValues(byte[] buffer, int srcOffset, boolean isDelta, Object[] result) {
        int rowLen = 0;
        int offset = srcOffset;
        for (int i = 0; i < readers.length; i++) {
            Reader reader = readers[i];
            ValueAndLength valueAndLength = isDelta ? reader.readDelta(buffer, offset) : reader.readRaw(buffer, offset);
            result[i] = valueAndLength.value;
            offset += valueAndLength.length;
            rowLen += valueAndLength.length;
        }
        return rowLen;
    }

    public int read(byte[] buffer, int offset, Object[] result) {
        // check if all values can be delta-ed or not
        boolean isDelta = bitCodec.read(buffer, offset, 1) == 1;
        return readValues(buffer, offset + 1, isDelta, result) + 1;
    }
}
