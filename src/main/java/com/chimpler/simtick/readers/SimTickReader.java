package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.readers.Reader;

import java.util.ArrayList;
import java.util.List;

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
            Object value = isDelta ? reader.readDelta(buffer, offset) : reader.readRaw(buffer, offset);
            result[i] = value;
            int len = isDelta ? reader.deltaBits : reader.rawBits;
            offset += len;
            rowLen += len;
        }
        return rowLen;
    }

    public int read(byte[] buffer, int offset, Object[] result) {
        // check if all values can be delta-ed or not
        boolean isDelta = bitCodec.read(buffer, offset, 1) == 1;
        return readValues(buffer, offset + 1, isDelta, result) + 1;
    }
}
